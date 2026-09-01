package com.nookure.staff.api.config.bukkit.partials.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PlayerActions {
    @Comment("""
      If true, the player will be able to inspect a player by
      shift and right-clicking on them.
      """)
    @Setting
    private boolean shiftAndRightClickToInspect = true;

    public boolean shiftAndRightClickToInspect() {
        return shiftAndRightClickToInspect;
    }
}
