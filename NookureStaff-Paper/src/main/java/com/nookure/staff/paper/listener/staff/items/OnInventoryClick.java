package com.nookure.staff.paper.listener.staff.items;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClick implements Listener {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        playerWrapperManager.getStaffPlayer(event.getWhoClicked().getUniqueId()).ifPresent(wrapper -> {
            if (wrapper.isInStaffMode()) {
                event.setCancelled(true);
            }
        });
    }
}
