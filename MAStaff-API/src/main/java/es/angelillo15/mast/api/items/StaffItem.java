package es.angelillo15.mast.api.items;

import org.bukkit.inventory.ItemStack;

public abstract class StaffItem {
    public abstract String getName();
    public abstract ItemStack getItem();
    public abstract void setItem(ItemStack item);
}
