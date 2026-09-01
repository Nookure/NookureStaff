package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.nookure.staff.api.annotation.PluginMessageSecretKey;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.common.PluginMessageConfig;
import com.nookure.staff.api.util.AbstractLoader;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.SecretKey;
import org.jetbrains.annotations.NotNull;

public class SecretKeyLoader implements AbstractLoader {
    private final ConfigurationContainer<PluginMessageConfig> config;
    private final AtomicReference<SecretKey> secretKey;

    @Inject
    public SecretKeyLoader(
            @NotNull final ConfigurationContainer<PluginMessageConfig> config,
            @NotNull @PluginMessageSecretKey final AtomicReference<SecretKey> secretKey) {
        this.config = config;
        this.secretKey = secretKey;
    }

    @Override
    public void load() {
        if (!config.get().enabled) {
            return;
        }

        secretKey.set(config.get().getSecretKey());
    }

    @Override
    public void reload() {
        load();
    }
}
