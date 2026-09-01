package com.nookure.staff.paper.listener.staff;

import com.google.inject.Inject;
import com.nookure.core.inv.paper.PaperNookureInventoryEngine;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.paper.inventory.InventoryList;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class OnShiftAndRightClick implements Listener {
    @Inject
    private AtomicReference<PaperNookureInventoryEngine> engine;

    @EventHandler
    private void onShiftAndRightClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player target)) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        if (!player.hasPermission(Permissions.PLAYER_ACTIONS_PERMISSION)
                || !player.hasPermission(Permissions.STAFF_PERMISSION)) {
            return;
        }

        engine.get()
                .openAsync(
                        player,
                        InventoryList.PLAYER_ACTIONS,
                        "uuid",
                        target.getUniqueId().toString());

        event.setCancelled(true);
    }
}
