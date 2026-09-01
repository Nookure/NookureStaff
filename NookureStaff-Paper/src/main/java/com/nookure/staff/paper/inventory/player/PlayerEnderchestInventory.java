package com.nookure.staff.paper.inventory.player;

import static java.util.Objects.requireNonNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlayerEnderchestInventory implements InventoryHolder {
    private final Player player;

    public PlayerEnderchestInventory(@NotNull Player player) {
        requireNonNull(player, "Player cannot be null");
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.PLAYER);
        inventory.setContents(player.getEnderChest().getContents());

        return inventory;
    }
}
