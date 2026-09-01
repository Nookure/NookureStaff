package com.nookure.staff.velocity.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaffPlatform;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.velocity.VelocityConfig;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.messaging.EventMessenger;
import com.nookure.staff.messaging.DecoderPluginMessenger;
import com.nookure.staff.velocity.NookureStaff;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.IOException;

public class VelocityPluginModule extends AbstractModule {
    private final NookureStaff plugin;

    public VelocityPluginModule(NookureStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Logger.class).toInstance(plugin.getPLogger());
        bind(com.nookure.staff.api.NookureStaff.class).toInstance(plugin);
        bind(EventManager.class).asEagerSingleton();
        bind(EventMessenger.class).to(DecoderPluginMessenger.class).asEagerSingleton();

        bind(new TypeLiteral<NookureStaffPlatform<ProxyServer>>() {}).toInstance(plugin);

        try {
            bind(new TypeLiteral<ConfigurationContainer<VelocityConfig>>() {}).toInstance(loadVelocity());
        } catch (IOException e) {
            plugin.getPLogger().severe("An error occurred while creating config files, %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private ConfigurationContainer<VelocityConfig> loadVelocity() throws IOException {
        ConfigurationContainer<VelocityConfig> config =
                ConfigurationContainer.load(plugin.getPluginDataFolder().toPath(), VelocityConfig.class);
        plugin.setDebug(config.get().isDebug());
        return config;
    }
}
