package com.nookure.staff.paper.listener.staff.items;

import com.nookure.staff.paper.inventory.player.PlayerEnderchestInventory;
import com.nookure.staff.paper.inventory.player.PlayerInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnPlayerInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder(false) instanceof PlayerInventory
                || event.getInventory().getHolder() instanceof PlayerEnderchestInventory) {
            event.setCancelled(true);
        }
    }
}
