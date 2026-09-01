package com.nookure.staff.api.item;

import com.nookure.staff.api.config.bukkit.partials.ItemPartial;
import com.nookure.staff.api.util.ServerUtils;
import com.nookure.staff.api.util.TextUtils;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Represents a staff item that can be added to the user's inventory.
 * this staff item does not have any functionality until it implements
 * an interface that represents a staff action.
 *
 * @since 1.0.0
 */
public abstract class StaffItem {
    public static final NamespacedKey key = new NamespacedKey("nkstaff-item", "staffitem");
    private final ItemPartial itemConfig;
    private final ItemStack itemStack;
    private final int slot;

    @SuppressWarnings("deprecation")
    public StaffItem(ItemPartial itemConfig) {
        this.itemConfig = itemConfig;
        ItemStack itemStack = new ItemStack(itemConfig.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();

        if (ServerUtils.isPaper) {
            Component displayName = TextUtils.toComponent(itemConfig.getName());
            displayName = displayName.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
            meta.displayName(displayName);

            meta.lore(itemConfig.lore());
        } else {
            meta.setDisplayName(
                    LegacyComponentSerializer.legacySection().serialize(TextUtils.toComponent(itemConfig.getName())));
            meta.setLore(itemConfig.lore().stream()
                    .map(cmp -> LegacyComponentSerializer.legacySection().serialize(cmp))
                    .toList());
        }

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, itemConfig.getSlot());

        slot = itemConfig.getSlot();

        itemStack.setItemMeta(meta);

        this.itemStack = itemStack;
    }

    public void setItem(Player playerWrapper) {
        playerWrapper.getInventory().setItem(itemConfig.getSlot(), itemStack);
    }

    public String getName() {
        return itemConfig.getName();
    }

    public String getPermission() {
        return itemConfig.getPermission();
    }

    public List<Component> getLore() {
        return itemConfig.lore();
    }

    public ItemPartial getItemConfig() {
        return itemConfig;
    }

    public int getSlot() {
        return slot;
    }
}
