package com.nookure.staff.api.addons;

import com.nookure.staff.api.addons.annotations.Addon;
import com.nookure.staff.api.command.Command;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AddonManager {
    /**
     * Retrieves a {@link AddonContainer} based on its instance.
     *
     * @param instance the instance
     * @return the container
     */
    Optional<AddonContainer> fromInstance(Object instance);

    /**
     * Retrieves a {@link AddonContainer} based on its ID.
     *
     * @param id the plugin ID
     * @return the plugin, if available
     */
    Optional<AddonContainer> getAddon(String id);

    /**
     * Gets a {@link Collection} of all {@link AddonContainer}s.
     *
     * @return the plugins
     */
    Collection<AddonContainer> getAddons();

    /**
     * Checks if an addon is enabled
     *
     * @param id The addon id
     * @return true if the addon is enabled
     */
    boolean isAddonEnabled(String id);

    /**
     * Loads all plugins from the specified {@code directory}.
     *
     * @param directory the directory to load from
     * @throws IOException if we could not open the directory
     */
    void loadAddonsToClasspath(Path directory) throws IOException;

    /**
     * Loads all the addons from the classpath.
     * This will get all the classes annotated with {@link Addon}
     * and load them.
     */
    void enableAllAddonsFromTheClasspath();

    /**
     * Enables an addon from its instance
     *
     * @param addon The addon instance
     */
    void enableAddon(Class<?> addon);

    /**
     * Enables all the addons that are currently loaded
     */
    void enableAllAddons();

    /**
     * Disables an addon from its instance
     *
     * @param addon The addon instance
     */
    void disableAddon(Object addon);

    /**
     * Disables all the addons that are currently loaded
     */
    void disableAllAddons();

    /**
     * Disables an addon from its id
     * if the addon does not exist, nothing will happen
     *
     * @param id The addon id
     */
    default void disableAddon(String id) {
        getAddon(id).ifPresent(this::disableAddon);
    }

    /**
     * Reloads an addon from its instance
     *
     * @param addon The addon instance
     */
    void reloadAddon(Object addon);

    /**
     * Reloads all the addons that are currently loaded
     * This will disable and enable all the addons
     * that are currently loaded
     */
    void reloadAllAddons();

    /**
     * Reloads an addon from its id
     *
     * @param id The addon id
     */
    default void reloadAddon(String id) {
        getAddon(id).ifPresent(this::reloadAddon);
    }

    /**
     * Destroys the classloader
     * This will remove all the addons from the classpath
     * and will destroy the classloader.
     * <p>
     * This will also disable all the addons
     * that are currently loaded
     *
     * @see #disableAllAddons()
     */
    void destroyClassloader();

    /**
     * Registers a listener to the event manager
     *
     * @param listener The listener
     * @param addon    The addon
     */
    void registerListener(Object listener, AddonContainer addon);

    /**
     * Registers a listener to the event manager
     *
     * @param listener The listener class
     * @param addon    The addon
     */
    void registerListeners(Class<?> listener, AddonContainer addon);

    /**
     * Unregisters a listener from the event manager
     *
     * @param listener The listener
     * @param addon    The addon
     */
    void unregisterListener(Object listener, AddonContainer addon);

    /**
     * Unregisters all the listeners from the event manager
     * that are registered by the addon
     *
     * @param addon The addon
     */
    void unregisterListeners(AddonContainer addon);

    /**
     * Registers a command to the command manager
     * The command must be annotated with {@link Command}
     *
     * @param command The command to register
     * @param addon   The addon
     */
    void registerCommand(Command command, AddonContainer addon);

    /**
     * Unregisters a command from the command manager
     * The command must be annotated with {@link com.nookure.staff.api.command.CommandData}
     *
     * @param addon The addon
     */
    void unregisterCommands(AddonContainer addon);

    /**
     * Gets all the loaded classloaders
     *
     * @return A list of all the loaded classloaders
     */
    List<ClassLoader> getLoaded();
}
