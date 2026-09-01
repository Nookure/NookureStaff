package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class OnEntityTarget implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) {
            return;
        }

        playerWrapperManager
                .getStaffPlayer(player.getUniqueId())
                .ifPresent(pw -> event.setCancelled(pw.isStaffModeOrVanish()));
    }
}
