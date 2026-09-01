package com.nookure.staff.api.config.bukkit.partials;

import com.nookure.staff.api.util.TextUtils;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ItemPartial {
    @Setting
    @Comment("""
      Whether the item is enabled or not.
      If the item is disabled, it won't be shown to anyone.
      """)
    private boolean enabled;

    @Setting
    @Comment("""
      The permission required to use the item.
      If the player doesn't have the permission, the item won't be shown.
      """)
    private String permission = "nookurestaff.item.x";

    @Setting
    @Comment("""
      The name of the item.
      The default format is mini message.
      but you can also use & or § to colorize the name.
      See hhttps://docs.advntr.dev/minimessage/format.html for more info.
      To put a hex color, use <#hexcode> or <gradient:[color1]:[color...]:[phase]> for gradients.
      """)
    private String name = "Item";

    @Setting
    @Comment("""
      The material of the item.
      See https://jd.papermc.io/paper/1.20/org/bukkit/Material.html for more info.
      """)
    private Material material = Material.MUSHROOM_STEW;

    @Setting
    @Comment("""
      The slot of the item.
      The slots are numbered from 0 to 8, from left to right.
      """)
    private int slot = 0;

    @Comment("""
      The lore of the item.
      The default format is mini message.
      but you can also use & or § to colorize the lore.
      See https://docs.advntr.dev/minimessage/format.html for more info.
      To put a hex color, use <#hexcode> or <gradient:[color1]:[color...]:[phase]> for gradients.
      """)
    @Setting
    private List<String> lore = List.of("Angelillo was here");

    public ItemPartial() {}

    public ItemPartial(
            boolean enabled, String name, Material material, int slot, List<String> lore, String permission) {
        this.enabled = enabled;
        this.name = name;
        this.material = material;
        this.slot = slot;
        this.lore = lore;
        this.permission = permission;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public Component name() {
        return TextUtils.toComponent(name);
    }

    public Material getMaterial() {
        return material;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<Component> lore() {
        return TextUtils.toComponent(lore);
    }

    public String getPermission() {
        return permission;
    }
}
