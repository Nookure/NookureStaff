package com.nookure.staff.paper.listener.staff.state;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;

public class OnBlockReceiveGameEvent implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onBlockReceiveGameEvent(BlockReceiveGameEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        playerWrapperManager.getStaffPlayer(player.getUniqueId()).ifPresent(playerWrapper -> {
            if (playerWrapper.isStaffModeOrVanish()) {
                event.setCancelled(true);
            }
        });
    }
}
