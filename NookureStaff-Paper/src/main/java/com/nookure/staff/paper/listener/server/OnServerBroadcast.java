package com.nookure.staff.paper.listener.server;

import com.google.inject.Inject;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.event.NookSubscribe;
import com.nookure.staff.api.event.server.BroadcastMessage;
import com.nookure.staff.api.event.server.BroadcastMessageExcept;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnServerBroadcast {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @NookSubscribe
    public void onBroadcast(BroadcastMessage event) {
        playerWrapperManager.stream()
                .filter(player -> player.hasPermission(event.permission()))
                .forEach(player -> player.sendMiniMessage(event.message()));
        if (event.showInConsole())
            Bukkit.getConsoleSender()
                    .sendMessage(TextUtils.toComponent(
                            event.message().replace("{prefix}", messages.get().prefix())));
    }

    @NookSubscribe
    public void onBroadcastStaffMessage(BroadcastMessageExcept event) {
        playerWrapperManager.stream()
                .filter(player -> player.hasPermission(event.permission())
                        && !(player.getUniqueId().equals(event.except())))
                .forEach(player -> player.sendMiniMessage(event.message()));

        Bukkit.getConsoleSender()
                .sendMessage(TextUtils.toComponent(
                        event.message().replace("{prefix}", messages.get().prefix())));
    }
}
