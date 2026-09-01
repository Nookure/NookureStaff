package com.nookure.staff.paper.listener.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitConfig;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.api.util.TextUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.Optional;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OnPlayerInStaffChatTalk implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Inject
    private ConfigurationContainer<BukkitConfig> config;

    @Inject
    private ServerUtils serverUtils;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void onPlayerInStaffChatTalk(AsyncChatEvent event) {
        Optional<StaffPlayerWrapper> optionalStaffPlayerWrapper =
                playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId());

        if (optionalStaffPlayerWrapper.isEmpty()) return;

        StaffPlayerWrapper staffPlayerWrapper = optionalStaffPlayerWrapper.get();

        if (!staffPlayerWrapper.isStaffChatAsDefault()) {
            return;
        }

        String message = messages.get().staffChat.format();

        message = TextUtils.parsePlaceholdersWithPAPI(event.getPlayer(), message);

        message = message.replace("{player}", staffPlayerWrapper.getName())
                .replace("{server}", config.get().getServerName())
                .replace("{message}", PlainTextComponentSerializer.plainText().serialize(event.message()));

        serverUtils.broadcast(message, Permissions.STAFF_CHAT, config.get().staffChat.logStaffChatInConsole);

        event.setCancelled(true);
    }
}
