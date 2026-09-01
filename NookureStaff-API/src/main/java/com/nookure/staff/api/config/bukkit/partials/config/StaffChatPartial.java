package com.nookure.staff.api.config.bukkit.partials.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StaffChatPartial {
    @Setting
    @Comment("If this is enabled, the staff chat will be logged in the console.")
    public boolean logStaffChatInConsole = true;
}
