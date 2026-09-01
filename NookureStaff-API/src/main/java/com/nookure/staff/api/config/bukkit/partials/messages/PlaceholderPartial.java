package com.nookure.staff.api.config.bukkit.partials.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PlaceholderPartial {
    @Setting
    @Comment("""
          The result to display when a placeholder displays an affirmative value.
              """)
    private String placeholderTrue = "<green>✔";

    @Setting
    @Comment("""
          The result to display when a placeholder displays a negative value.
              """)
    private String placeholderFalse = "<red>✘";

    public String placeholderTrue() {
        return placeholderTrue;
    }

    public String placeholderFalse() {
        return placeholderFalse;
    }
}
