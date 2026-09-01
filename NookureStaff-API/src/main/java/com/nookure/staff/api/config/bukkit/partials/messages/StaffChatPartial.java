package com.nookure.staff.api.config.bukkit.partials.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StaffChatPartial {
    @Setting
    @Comment("The format for sent messages on Staff Chat.")
    private String format =
            "<b><dark_aqua>SC</dark_aqua></b> <green>{player} <dark_gray>({server}) <dark_gray><bold>»</bold> <gray>{message}";

    @Setting
    @Comment("Enable the staff chat as main chat.")
    private String enable = "{prefix} <green>You have enabled the staff chat as main chat!";

    @Setting
    @Comment("Disable the staff chat as main chat.")
    private String disable = "{prefix} <red>You have disabled the staff chat as main chat!";

    public String format() {
        return format;
    }

    public String enable() {
        return enable;
    }

    public String disable() {
        return disable;
    }
}
