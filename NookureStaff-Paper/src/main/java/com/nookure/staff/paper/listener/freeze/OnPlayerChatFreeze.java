package com.nookure.staff.paper.listener.freeze;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.util.ServerUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnPlayerChatFreeze implements Listener {
    @Inject
    private FreezeManager freezeManager;

    @Inject
    private ServerUtils serverUtils;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!freezeManager.isFrozen(event.getPlayer())) return;

        event.setCancelled(true);

        String message = messages.get()
                .freeze
                .freezeChatFormat()
                .replace("{player}", event.getPlayer().getName())
                .replace("{message}", PlainTextComponentSerializer.plainText().serialize(event.message()));

        playerWrapperManager
                .getPlayerWrapper(event.getPlayer())
                .ifPresent(playerWrapper -> playerWrapper.sendMiniMessage(message));

        serverUtils.broadcast(message, Permissions.STAFF_FREEZE);

        freezeManager.getFreezeContainer(event.getPlayer().getUniqueId()).ifPresent(freezeContainer -> {
            freezeContainer.setHasTalked(true);
        });
    }
}
