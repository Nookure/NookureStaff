package com.nookure.staff.paper.listener.staff.items;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.item.PlayerInteractItem;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class OnPlayerEntityInteract extends CommonPlayerInteraction implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private Logger logger;

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        long start = System.currentTimeMillis();
        Player player = event.getPlayer();

        if (playerWrapperManager.getStaffPlayer(player.getUniqueId()).isEmpty()) {
            return;
        }

        StaffPlayerWrapper playerWrapper =
                playerWrapperManager.getStaffPlayer(player.getUniqueId()).get();

        if (!playerWrapper.isInStaffMode()) return;

        event.setCancelled(true);

        ItemStack currentItem = player.getInventory().getItemInMainHand();

        if ((currentItem.getType().isAir() || (currentItem.getAmount() <= 0))
                && player.hasPermission(Permissions.STAFF_MODE_BUILD)) {
            event.setCancelled(false);
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!canUseItem(playerWrapper)) return;

        getItem(currentItem, playerWrapper).ifPresent(item -> {
            if (item instanceof PlayerInteractItem executableItem) {
                if (!(event.getRightClicked() instanceof Player target)) return;

                playerWrapperManager.getPlayerWrapper(target).ifPresent(targetWrapper -> {
                    executableItem.click(playerWrapper, targetWrapper);
                });
            }
        });

        logger.debug("PlayerInteractEntityEvent took " + (System.currentTimeMillis() - start) + "ms");
    }
}
