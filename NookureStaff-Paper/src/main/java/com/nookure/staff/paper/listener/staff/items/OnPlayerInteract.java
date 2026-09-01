package com.nookure.staff.paper.listener.staff.items;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.item.ExecutableItem;
import com.nookure.staff.api.item.ExecutableLocationItem;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class OnPlayerInteract extends CommonPlayerInteraction implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private Logger logger;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        long start = System.currentTimeMillis();
        Player player = event.getPlayer();

        if (playerWrapperManager.getStaffPlayer(player.getUniqueId()).isEmpty()) {
            return;
        }

        StaffPlayerWrapper playerWrapper =
                playerWrapperManager.getStaffPlayer(player.getUniqueId()).get();

        if (!playerWrapper.isInStaffMode()) return;

        event.setCancelled(true);

        if (!event.hasItem() && player.hasPermission(Permissions.STAFF_MODE_BUILD)) {
            event.setCancelled(false);
        }

        if (event.getHand() != EquipmentSlot.HAND) return;

        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;

        if (!canUseItem(playerWrapper)) return;

        getItem(event.getItem(), playerWrapper).ifPresent(item -> {
            if (item instanceof ExecutableItem executableItem) executableItem.click(playerWrapper);

            if (item instanceof ExecutableLocationItem executableIcon) {
                if (event.getClickedBlock() == null) return;

                executableIcon.click(playerWrapper, event.getClickedBlock().getLocation());
            }
        });

        logger.debug("PlayerInteractEvent took " + (System.currentTimeMillis() - start) + "ms");
    }
}
