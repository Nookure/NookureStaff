package com.nookure.staff.paper.messaging;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.annotation.PluginMessageSecretKey;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.common.PluginMessageConfig;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.exception.DataIntegrityCheckFailedException;
import com.nookure.staff.api.messaging.Channels;
import com.nookure.staff.api.messaging.EventMessenger;
import com.nookure.staff.api.service.EncryptService;
import java.util.concurrent.atomic.AtomicReference;
import javax.crypto.SecretKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BackendMessageMessenger extends EventMessenger implements PluginMessageListener {
    private final EncryptService encryptService;
    private final EventManager eventManager;
    private final Logger logger;
    private final ConfigurationContainer<PluginMessageConfig> config;
    private final AtomicReference<SecretKey> secretKey;

    @Inject
    public BackendMessageMessenger(
            @NotNull final EventManager eventManager,
            @NotNull final Logger logger,
            @NotNull final NookureStaff plugin,
            @NotNull final EncryptService encryptService,
            @NotNull final ConfigurationContainer<PluginMessageConfig> config,
            @NotNull @PluginMessageSecretKey final AtomicReference<SecretKey> secretKey) {
        super(logger, plugin);
        this.eventManager = eventManager;
        this.logger = logger;
        this.config = config;
        this.encryptService = encryptService;
        this.secretKey = secretKey;
    }

    @Override
    public void prepare() {
        // Nothing to do here
    }

    @Override
    public void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data) {
        if (config.get().enabled) {
            data = encryptService.encrypt(data, secretKey.get());
        }

        sender.sendPluginMessage(Channels.EVENTS, data);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(Channels.EVENTS)) {
            return;
        }

        try {
            logger.debug("Received event from plugin message");

            if (config.get().enabled) {
                try {
                    message = encryptService.decrypt(message, secretKey.get());
                } catch (DataIntegrityCheckFailedException e) {
                    logger.severe(
                            "The player %s sent a message with an invalid integrity check".formatted(player.getName()));
                    if (config.get().playerTamperingDetection) {
                        player.kick(
                                Component.text(config.get().playerTamperingDetectionMessage),
                                PlayerKickEvent.Cause.ILLEGAL_ACTION);
                    }
                    return;
                } catch (Exception e) {
                    logger.severe("Failed to decrypt message");
                    return;
                }
            }

            decodeEvent(message).ifPresent(eventManager::fireEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
