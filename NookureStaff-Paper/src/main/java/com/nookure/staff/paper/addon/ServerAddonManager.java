package com.nookure.staff.paper.addon;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.addons.*;
import com.nookure.staff.api.addons.annotations.Addon;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.event.addon.AddonDisableEvent;
import com.nookure.staff.api.event.addon.AddonEnableEvent;
import com.nookure.staff.api.event.addon.AddonReloadEvent;
import com.nookure.staff.api.exception.AddonException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Singleton
public class ServerAddonManager implements AddonManager {
    private final Map<String, AddonContainer> addonsById = new ConcurrentHashMap<>();
    private final Map<Object, AddonContainer> addonsInstances = new IdentityHashMap<>();
    private final Map<AddonContainer, List<Object>> listeners = new HashMap<>();
    private final Map<AddonContainer, List<Command>> commands = new HashMap<>();
    private final ArrayList<ClassLoader> loaded = new ArrayList<>();

    @Inject
    private NookureStaff instance;

    @Inject
    private Logger logger;

    @Inject
    private EventManager eventManager;

    private void registerAddon(AddonContainer addonContainer) {
        addonsById.put(addonContainer.getDescription().getID(), addonContainer);
        addonsInstances.put(addonContainer.getInstance(), addonContainer);
    }

    @Override
    public Optional<AddonContainer> fromInstance(Object instance) {
        return Optional.ofNullable(addonsInstances.get(instance));
    }

    @Override
    public Optional<AddonContainer> getAddon(String id) {
        return Optional.ofNullable(addonsById.get(id));
    }

    @Override
    public Collection<AddonContainer> getAddons() {
        return addonsById.values();
    }

    @Override
    public boolean isAddonEnabled(String id) {
        return getAddon(id).isPresent() && getAddon(id).get().getStatus() == AddonStatus.ENABLED;
    }

    @Override
    public void loadAddonsToClasspath(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                directory, p -> p.toFile().isFile() && p.toString().endsWith(".jar"))) {
            for (Path path : stream) {
                URL[] urls = new URL[] {path.toUri().toURL()};
                URLClassLoader loader =
                        new URLClassLoader(urls, instance.getClass().getClassLoader());

                Properties properties = getAddonProperties(path.toFile());

                if (properties == null) {
                    logger.severe("Could not load addon %s properties", path.getFileName());
                    return;
                }

                String main = properties.getProperty("main-class");
                if (main == null) throw new AddonException("Main class not found in addon properties");

                try {
                    Class<?> clazz = loader.loadClass(main);
                    if (clazz.isAnnotationPresent(Addon.class)) enableAddon(clazz);
                } catch (ClassNotFoundException e) {
                    throw new AddonException("Error occurred while loading addons to classpath", e);
                }

                loaded.add(loader);
                logger.debug("Addon " + path.getFileName() + " added to classpath");
            }
        } catch (Exception e) {
            throw new AddonException("Error occurred while loading addons to classpath", e);
        }
    }

    private ArrayList<JarEntry> getJarEntries(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            return Collections.list(jar.entries());
        } catch (IOException e) {
            throw new AddonException("Error occurred while getting jar entries", e);
        }
    }

    private Properties getAddonProperties(File file) {
        Properties properties = new Properties();
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry entry = jarFile.getJarEntry("addon.properties");
            properties.load(jarFile.getInputStream(entry));
            return properties;
        } catch (IOException e) {
            logger.severe("Could not load addon %s", file.getName());
            logger.severe("An error ocurred while reading the file, please check the file and try again");
            logger.severe(e);
            return null;
        }
    }

    @Override
    public void enableAllAddonsFromTheClasspath() {
        AddonsUtils.getAddons(Addon.AddonPlatform.BUKKIT).forEach(this::enableAddon);
    }

    @Override
    public void enableAddon(Class<?> addonClass) {
        AddonContainer addonContainer = new ServerAddonContainer(new AddonDescriptionBuilder()
                .setAddon(addonClass.getAnnotation(Addon.class))
                .build());

        logger.debug("Enabling addon " + addonContainer.getDescription().getID());

        Injector addonInjector =
                instance.getInjector().createChildInjector(new AddonCommonModule(addonContainer, instance, addonClass));

        Object addon = addonInjector.getInstance(addonClass);

        addonContainer.setInstance(addon);
        addonContainer.setStatus(AddonStatus.ENABLED);

        registerAddon(addonContainer);

        for (Class<?> listener : addonContainer.getAddon().listeners()) {
            registerListener(addonInjector.getInstance(listener), addonContainer);
        }

        for (Class<? extends Command> commands : addonContainer.getAddon().commands()) {
            registerCommand(addonInjector.getInstance(commands), addonContainer);
        }

        if (addon instanceof AddonActions actions) actions.onEnable();

        eventManager.fireEvent(new AddonEnableEvent(addonContainer));
    }

    @Override
    public void enableAllAddons() {
        enableAllAddonsFromTheClasspath();
    }

    @Override
    public void disableAddon(Object addon) {
        fromInstance(addon).ifPresent(addonContainer -> {
            addonContainer.setStatus(AddonStatus.DISABLED);
            addonsById.remove(addonContainer.getDescription().getID());
            addonsInstances.remove(addonContainer.getInstance());
            unregisterListeners(addonContainer);
            unregisterCommands(addonContainer);

            if (addon instanceof AddonActions actions) actions.onDisable();

            eventManager.fireEvent(new AddonDisableEvent(addonContainer));
        });
    }

    @Override
    public void disableAllAddons() {
        addonsById.values().forEach(addonContainer -> disableAddon(addonContainer.getInstance()));
    }

    @Override
    public void reloadAddon(Object addon) {
        fromInstance(addon).ifPresent(addonContainer -> {
            if (addon instanceof AddonActions actions) actions.onReload();
            eventManager.fireEvent(new AddonReloadEvent(addonContainer));
        });
    }

    @Override
    public void reloadAllAddons() {
        getAddons().forEach(addonContainer -> reloadAddon(addonContainer.getInstance()));
    }

    @Override
    public void destroyClassloader() {
        disableAllAddons();
        loaded.forEach(loader -> {
            try {
                if (loader instanceof URLClassLoader urlClassLoader) urlClassLoader.close();
            } catch (IOException e) {
                throw new AddonException("Error occurred while destroying addons ClassLoaders", e);
            }
        });
    }

    @Override
    public void registerListener(Object listener, AddonContainer addon) {
        if (!listeners.containsKey(addon)) listeners.put(addon, new ArrayList<>());
        logger.debug("Registering listener " + listener.getClass().getSimpleName() + " for addon "
                + addon.getDescription().getID());
        listeners.get(addon).add(listener);
        eventManager.registerListener(listener);
    }

    @Override
    public void registerListeners(Class<?> listener, AddonContainer addon) {
        registerListener(instance.getInjector().getInstance(listener), addon);
    }

    @Override
    public void unregisterListener(Object listener, AddonContainer addon) {
        if (listeners.containsKey(addon)) {
            listeners.get(addon).remove(listener);
            eventManager.unregisterListener(listener);
        }
    }

    @Override
    public void unregisterListeners(AddonContainer addon) {
        if (listeners.containsKey(addon)) {
            listeners.get(addon).forEach(listener -> eventManager.unregisterListener(listener));
            listeners.remove(addon);
        }
    }

    @Override
    public void registerCommand(Command command, AddonContainer addon) {
        if (!commands.containsKey(addon)) commands.put(addon, new ArrayList<>());
        logger.debug("Registering command " + command.getClass().getSimpleName() + " for addon "
                + addon.getDescription().getID());
        commands.get(addon).add(command);
        instance.registerCommand(command);
    }

    @Override
    public void unregisterCommands(AddonContainer addon) {
        if (commands.containsKey(addon)) {
            commands.get(addon).forEach(command -> instance.unregisterCommand(command));
            commands.remove(addon);
        }
    }

    @Override
    public List<ClassLoader> getLoaded() {
        return loaded;
    }
}
