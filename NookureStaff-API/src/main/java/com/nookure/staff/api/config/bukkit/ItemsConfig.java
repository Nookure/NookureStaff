package com.nookure.staff.api.config.bukkit;

import com.nookure.staff.api.config.bukkit.partials.CustomItemPartial;
import com.nookure.staff.api.config.bukkit.partials.CustomItemType;
import com.nookure.staff.api.config.bukkit.partials.ItemPartial;
import com.nookure.staff.api.item.Items;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ItemsConfig {
    public final StaffItems staffItems = new StaffItems();

    @ConfigSerializable
    public static class StaffItems {
        @Setting
        private Map<String, ItemPartial> items = new HashMap<>();

        @Setting
        private Map<String, CustomItemPartial> customItems = new HashMap<>();

        public StaffItems() {
            items.put(
                    Items.RANDOM_PLAYER_TELEPORT.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Random Teleport",
                            Material.ENDER_PEARL,
                            0,
                            List.of("<gray>Right click to teleport to a random player"),
                            "nookurestaff.item.randomteleport"));

            items.put(
                    Items.FREEZE.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Freeze",
                            Material.BLAZE_ROD,
                            1,
                            List.of("<gray>Click to freeze", "<gray>The player you are looking at"),
                            "nookurestaff.item.freeze"));

            items.put(
                    Items.ENDER_CHEST.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Enderchest",
                            Material.ENDER_CHEST,
                            3,
                            List.of("<gray>Click to open the target's enderchest"),
                            "nookurestaff.item.enderchest"));

            items.put(
                    Items.VANISH.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Vanish",
                            Material.ENDER_EYE,
                            4,
                            List.of("<gray>Click to toggle vanish"),
                            "nookurestaff.item.vanish"));

            items.put(
                    Items.INVSEE.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Invsee",
                            Material.CHEST,
                            5,
                            List.of("<gray>Click to open the inventory", "<gray>Of the player you are looking at"),
                            "nookurestaff.item.invsee"));

            items.put(
                    Items.NIGHT_VISION.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Night Vision",
                            Material.POTION,
                            7,
                            List.of("<gray>Click to toggle night vision"),
                            "nookurestaff.item.nightvision"));

            items.put(
                    Items.THRU.toString(),
                    new ItemPartial(
                            true,
                            "<blue>Thru",
                            Material.COMPASS,
                            8,
                            List.of("<gray>Click to teleport through", "<gray>The block you are looking at"),
                            "nookurestaff.item.thru"));

            customItems.put(
                    "custom_item",
                    new CustomItemPartial(
                            false,
                            "<blue>Custom Item",
                            Material.DIAMOND,
                            2,
                            List.of("<gray>Click to send hello world to the player that you are looking at"),
                            "nookurestaff.item.custom",
                            CustomItemType.COMMAND_TARGET_AS_PLAYER,
                            "msg {target} Hello, world!"));

            customItems.put(
                    "custom_item_2",
                    new CustomItemPartial(
                            false,
                            "<blue>Custom Item 2",
                            Material.REDSTONE_BLOCK,
                            6,
                            List.of("<gray>Click to kick the player that you are looking at"),
                            "nookurestaff.item.custom2",
                            CustomItemType.COMMAND_TARGET_AS_CONSOLE,
                            "kick {target} Hello, world!"));
        }

        public Map<String, ItemPartial> getItems() {
            return items;
        }

        public Map<String, CustomItemPartial> getCustomItems() {
            return customItems;
        }
    }
}
