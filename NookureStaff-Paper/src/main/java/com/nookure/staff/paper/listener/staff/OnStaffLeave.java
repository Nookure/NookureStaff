package com.nookure.staff.paper.listener.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnStaffLeave implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler(ignoreCancelled = true)
    public void onStaffLeave(PlayerQuitEvent event) {
        Optional<StaffPlayerWrapper> optional =
                playerWrapperManager.getStaffPlayer(event.getPlayer().getUniqueId());

        if (optional.isEmpty()) {
            return;
        }

        StaffPaperPlayerWrapper wrapper = (StaffPaperPlayerWrapper) optional.get();
        wrapper.unregisterExtensions();

        if (wrapper.isInStaffMode()) {
            wrapper.clearInventory();
            wrapper.restoreInventory();
            wrapper.disablePlayerPerks();
        }
    }
}
