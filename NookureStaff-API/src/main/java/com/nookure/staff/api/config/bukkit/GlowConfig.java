package com.nookure.staff.api.config.bukkit;

import java.util.Map;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class GlowConfig {
    @Setting
    @Comment("Enable or disable the glow feature")
    public boolean enabled = true;

    @Setting
    @Comment("Change the prefix color of the player's name")
    public boolean tabIntegration = true;

    @Setting
    @Comment("The default color for the glow")
    public String defaultColor = "<white>";

    @Setting
    @Comment("This colors must follow the format: <color> or <#hex>")
    public Map<String, String> glowColors = Map.of(
            "staff", "<red>",
            "admin", "<yellow>",
            "mod", "<green>");
}
