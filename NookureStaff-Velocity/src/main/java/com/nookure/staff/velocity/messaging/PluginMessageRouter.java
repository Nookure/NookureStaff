package com.nookure.staff.velocity.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.messaging.Channels;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.jetbrains.annotations.NotNull;

public class PluginMessageRouter {
    public static final MinecraftChannelIdentifier EVENTS = MinecraftChannelIdentifier.from(Channels.EVENTS);
    public static final MinecraftChannelIdentifier COMMANDS = MinecraftChannelIdentifier.from(Channels.COMMANDS);

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    @Subscribe
    public void onPluginMessageFromPlayer(PluginMessageEvent event) {
        if (event.getIdentifier() == EVENTS) {
            manageForward(event);
        }

        if (event.getIdentifier() == COMMANDS) {
            manageCommand(event);
        }
    }

    public void manageForward(@NotNull final PluginMessageEvent event) {
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            logger.warning("Received plugin message from non-server connection source.");
            logger.warning("Source: %s", event.getSource());
            logger.warning("Be aware that this is a potential player trying to attack your server.");
            return;
        }

        logger.debug("Routing plugin message to all servers.");

        server.getAllServers().forEach(server -> {
            if (server.sendPluginMessage(EVENTS, event.getData()))
                logger.debug("Sent plugin message to %s", server.getServerInfo().getName());
        });
    }

    public void manageCommand(@NotNull final PluginMessageEvent event) {
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            logger.warning("Received plugin message from non-server connection source.");
            logger.warning("Source: %s", event.getSource());
            logger.warning("Be aware that this is a potential player trying to attack your server.");
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();

        String command = in.readUTF();

        logger.debug("Received command: %s", command);

        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
    }
}
