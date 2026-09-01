package com.nookure.staff.paper.listener.staff.command;

import com.google.inject.Inject;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.StaffModeBlockedCommands;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnStaffPlayerCommand implements Listener {
    @Inject
    private ConfigurationContainer<StaffModeBlockedCommands> blockedCommands;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onStaffPlayerCommand(PlayerCommandPreprocessEvent event) {
        Optional<StaffPlayerWrapper> optional =
                playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId());

        if (optional.isEmpty()) {
            return;
        }

        StaffPlayerWrapper wrapper = optional.get();

        if (!wrapper.isInStaffMode()) return;
        if (wrapper.hasPermission(Permissions.STAFF_MODE_COMMANDS_BYPASS)) return;

        if (blockedCommands
                .get()
                .getBlockedCommands()
                .contains(event.getMessage().split(" ")[0].substring(1))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(blockedCommands.get().getBlockedCommandUsage());
        }
    }
}
