package com.nookure.staff.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.staff.Constants;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaffPlatform;
import com.nookure.staff.velocity.messaging.PluginMessageRouter;
import com.nookure.staff.velocity.module.VelocityLogger;
import com.nookure.staff.velocity.module.VelocityPluginModule;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

@Plugin(
        id = "nookurestaff",
        name = "NookureStaff",
        description = "The Bridge for NookureStaff backends",
        authors = {"Angelillo15", "Nookure"},
        version = Constants.VERSION)
public class NookureStaff implements NookureStaffPlatform<ProxyServer> {
    private final ClassLoader classLoader = getClass().getClassLoader();

    @Inject
    private ProxyServer server;

    @Inject
    private Injector injector;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private Logger logger;
    private boolean debug;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.logger = new VelocityLogger(server, this);
        this.injector = injector.createChildInjector(new VelocityPluginModule(this));

        server.getChannelRegistrar().register(PluginMessageRouter.EVENTS);
        server.getChannelRegistrar().register(PluginMessageRouter.COMMANDS);
        server.getEventManager().register(this, injector.getInstance(PluginMessageRouter.class));

        logger.info("NookureStaff velocity bridge has been enabled!.");
        logger.debug("Debug mode is enabled.");
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
    public void reload() {}

    @Override
    public File getPluginDataFolder() {
        return dataDirectory.toFile();
    }

    @Override
    public InputStream getPluginResource(String s) {
        return classLoader.getResourceAsStream(s);
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public String getPrefix() {
        return "NookureStaff";
    }

    @Override
    public ProxyServer getPlatform() {
        return server;
    }

    public ProxyServer server() {
        return server;
    }
}
