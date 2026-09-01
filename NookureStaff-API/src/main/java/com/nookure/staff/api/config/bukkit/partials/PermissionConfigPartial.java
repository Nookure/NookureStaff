package com.nookure.staff.api.config.bukkit.partials;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PermissionConfigPartial {
    @Setting
    @Comment("Listen when a player gets `nookure.staff` permission to reconstruct the player wrapper.")
    public boolean watchLuckPermsPermissions = true;
}
