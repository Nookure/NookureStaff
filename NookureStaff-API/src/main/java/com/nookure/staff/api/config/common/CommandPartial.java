package com.nookure.staff.api.config.common;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandPartial {
    @Setting
    @Comment("The name of the command")
    private String name;

    @Setting
    @Comment("The aliases of the command")
    private List<String> aliases;

    @Setting
    @Comment("The permission required to execute the command")
    private String permission;

    @Setting
    @Comment("The description of the command")
    private String description;

    @Setting
    @Comment("The usage of the command")
    private String usage;

    @Setting
    @Comment("Whether the command is enabled")
    private boolean enabled;

    public CommandPartial(
            String name, List<String> aliases, String permission, String description, String usage, boolean enabled) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.enabled = enabled;
    }

    public CommandPartial() {}

    public String name() {
        return name;
    }

    public List<String> aliases() {
        return aliases;
    }

    public String permission() {
        return permission;
    }

    public String description() {
        return description;
    }

    public String usage() {
        return usage;
    }

    public boolean enabled() {
        return enabled;
    }
}
