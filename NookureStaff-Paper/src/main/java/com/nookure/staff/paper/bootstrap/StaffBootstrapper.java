package com.nookure.staff.paper.bootstrap;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.LibraryManager;
import com.alessiodp.libby.PaperLibraryManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nookure.staff.Constants;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaffPlatform;
import com.nookure.staff.api.command.Command;
import com.nookure.staff.api.jenkins.JenkinsBaseClient;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.lib.DefaultLibRepo;
import com.nookure.staff.paper.NookureStaff;
import com.nookure.staff.paper.util.BukkitLoggerImpl;
import com.nookure.staff.paper.util.Metrics;
import com.nookure.staff.paper.util.PaperLoggerImpl;
import java.io.File;
import java.io.InputStream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffBootstrapper extends JavaPlugin implements NookureStaffPlatform<JavaPlugin> {
    public static final boolean isPaper;
    public static final boolean isMock;
    public static final String JENKINS_URL;

    static {
        isPaper = ServerUtils.isPaper;
    }

    static {
        boolean isMockTemp;
        try {
            isMockTemp = Bukkit.getName().equalsIgnoreCase("servermock");
        } catch (Exception e) {
            isMockTemp = false;
        }

        isMock = isMockTemp;

        JENKINS_URL = System.getProperty("nkStaffJenkinsHost") != null
                ? System.getProperty("nkStaffJenkinsHost")
                : "https://ci.nookure.com";
    }

    private boolean debug = false;
    private Injector injector;
    private Logger logger;
    private NookureStaff plugin;

    @Override
    public void onEnable() {
        checkSpigot();

        Component cmp = Component.text("""

         ▐ ▄             ▄ •▄ ▄• ▄▌▄▄▄  ▄▄▄ .    .▄▄ · ▄▄▄▄▄ ▄▄▄· ·▄▄▄·▄▄▄
        •█▌▐█▪     ▪     █▌▄▌▪█▪██▌▀▄ █·▀▄.▀·    ▐█ ▀. •██  ▐█ ▀█ ▐▄▄·▐▄▄·
        ▐█▐▐▌ ▄█▀▄  ▄█▀▄ ▐▀▀▄·█▌▐█▌▐▀▀▄ ▐▀▀▪▄    ▄▀▀▀█▄ ▐█.▪▄█▀▀█ ██▪ ██▪
        ██▐█▌▐█▌.▐▌▐█▌.▐▌▐█.█▌▐█▄█▌▐█•█▌▐█▄▄▌    ▐█▄▪▐█ ▐█▌·▐█ ▪▐▌██▌.██▌.
        ▀▀ █▪ ▀█▄▀▪ ▀█▄▀▪·▀  ▀ ▀▀▀ .▀  ▀ ▀▀▀      ▀▀▀▀  ▀▀▀  ▀  ▀ ▀▀▀ ▀▀▀
        """).color(NamedTextColor.LIGHT_PURPLE);

        sendComponent(cmp);

        sendComponent(Component.text("NookureStaff v" + Constants.VERSION + " by Angelillo15")
                .color(NamedTextColor.LIGHT_PURPLE));

        loadLogger();
        loadDependencies();
        checkMAStaff();
        checkVersion();

        loadInjector();
        loadPlugin();
        new Metrics(this, 16548);

        plugin.onEnable();
    }

    public void checkVersion() {
        if (Constants.JENKINS_BUILD_NUMBER == -1) {
            getLogger().warning("You are using a development build. Please, consider this before reporting any issue.");
            return;
        }

        JenkinsBaseClient jenkinsBaseClient = new JenkinsBaseClient(JENKINS_URL);

        jenkinsBaseClient
                .getJob(Constants.JENKINS_JOB_NAME)
                .thenAccept(jenkinsJob -> {
                    if (jenkinsJob == null) {
                        getLogger().warning("Couldn't get the latest version from Jenkins.");
                        return;
                    }

                    if (jenkinsJob.lastCompletedBuild() == null) {
                        getLogger().warning("Couldn't get the latest version from Jenkins.");
                        return;
                    }

                    if (jenkinsJob.lastCompletedBuild().number() > Constants.JENKINS_BUILD_NUMBER) {
                        getLogger()
                                .warning("There is a new version available! You are %n versions behind."
                                        .replace(
                                                "%n",
                                                String.valueOf(jenkinsJob
                                                                .lastCompletedBuild()
                                                                .number()
                                                        - Constants.JENKINS_BUILD_NUMBER)));
                        getLogger()
                                .warning("You can download it from here: "
                                        + jenkinsJob.lastCompletedBuild().url());
                        getLogger().warning("Or you can download it from here: https://modrinth.com/plugin/staff");
                    }
                })
                .exceptionally(throwable -> {
                    getLogger().warning("Couldn't get the latest version from Jenkins.");
                    return null;
                });
    }

    public void checkMAStaff() {
        if (Bukkit.getPluginManager().getPlugin("MAStaff") != null) {
            sendComponent(Component.text("MAStaff detected!").color(NamedTextColor.RED));
            sendComponent(Component.text("Please remove it to avoid conflicts.").color(NamedTextColor.RED));
            sendComponent(Component.text("Disabling NookureStaff...").color(NamedTextColor.RED));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void checkSpigot() {
        if ((isPaper) || (Bukkit.getPluginManager().getPlugin("NookureStaff-Compatibility") != null)) {
            return;
        }

        if (isMock) return;

        getLogger().severe("------------------------------------------------------------------");
        getLogger().severe("DONT IGNORE THIS MESSAGE, IT'S IMPORTANT!");
        getLogger().severe("You are using Spigot, which is not recommended for NookureStaff.");
        getLogger().severe("Please, use Paper instead in most cases.");
        getLogger().severe("If you still want to use Spigot, you can download the compatibility plugin.");
        getLogger()
                .severe(
                        "Download it from here -> https://github.com/Nookure/NookureStaff-Compatibility/releases/download/NookureStaff-Compatibility-1.0.0/NookureStaff-Compatibility-1.0.0.jar");
        getLogger().severe("------------------------------------------------------------------");

        Bukkit.getPluginManager().disablePlugin(this);
    }

    private void sendComponent(Component component) {
        if (isPaper) {
            Bukkit.getConsoleSender().sendMessage(component);
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(LegacyComponentSerializer.legacySection().serialize(component));
        }
    }

    private void loadDependencies() {
        if (isMock) return;

        LibraryManager manager;

        try {
            Class.forName("io.papermc.paper.plugin.entrypoint.classloader.PaperPluginClassLoader");
            try {
                manager = new PaperLibraryManager(this);
            } catch (Exception e) {
                manager = new BukkitLibraryManager(this);
                logger.severe(e);
                logger.warning("------------------------------------------------------------------");
                logger.warning("DONT IGNORE THIS MESSAGE, IT'S IMPORTANT!");
                logger.warning("There was several issues while trying to load some paper-plugin related stuff.");
                logger.warning("This isn't an error exactly");
                logger.warning("Please consider using the latest paper version");
                logger.warning(
                        "If you are using the latest paper version, please report this issue to the plugin developer");
                logger.warning("------------------------------------------------------------------");
            }
        } catch (ClassNotFoundException e) {
            manager = new BukkitLibraryManager(this);
        }

        manager.addMavenCentral();
        manager.addJitPack();
        manager.addRepository("https://maven.nookure.com");

        DefaultLibRepo.getInstance().getLibraries().forEach(manager::loadLibrary);
    }

    @Override
    public void onDisable() {
        plugin.onDisable();
    }

    public void loadLogger() {
        logger = isPaper ? new PaperLoggerImpl(this) : new BukkitLoggerImpl(this);
    }

    public void loadInjector() {
        injector = Guice.createInjector(new PaperPluginModule(this));
        injector.injectMembers(this);
    }

    public void loadPlugin() {
        logger.info("💉  Injecting plugin...");
        plugin = injector.getInstance(NookureStaff.class);
        logger.info("💉  Plugin injected!");
    }

    @Override
    public JavaPlugin getPlatform() {
        return this;
    }

    @Override
    public Logger getPLogger() {
        return logger;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public void reload() {
        plugin.reload();
    }

    @Override
    public File getPluginDataFolder() {
        return getDataFolder();
    }

    @Override
    public InputStream getPluginResource(String s) {
        return getResource(s);
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public String getPrefix() {
        return plugin.getPrefix();
    }

    @Override
    public void registerCommand(Command command) {
        plugin.registerCommand(command);
    }

    @Override
    public void unregisterCommand(Command command) {
        plugin.unregisterCommand(command);
    }
}
