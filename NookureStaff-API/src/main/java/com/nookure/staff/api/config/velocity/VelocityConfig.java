package com.nookure.staff.api.config.velocity;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class VelocityConfig {
    @Setting
    @Comment("""
      Enable or disable debug mode.
      This will print out more information to the console.
      It's recommended to find bugs.
      """)
    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }
}
