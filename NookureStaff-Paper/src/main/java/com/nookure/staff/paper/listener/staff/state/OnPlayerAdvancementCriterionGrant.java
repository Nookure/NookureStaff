package com.nookure.staff.paper.listener.staff.state;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnPlayerAdvancementCriterionGrant implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementCriterionGrantEvent event) {
        final var staffPlayer =
                playerWrapperManager.getStaffPlayerOrNull(event.getPlayer().getUniqueId());

        if (staffPlayer == null) {
            return;
        }

        event.setCancelled(staffPlayer.isStaffModeOrVanish());
    }
}
