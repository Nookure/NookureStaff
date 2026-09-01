package com.nookure.staff.api.config.bukkit;

import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StaffModeBlockedCommands {
    @Setting
    @Comment("""
      The message that is sent to a player when they try to use a blocked command while in staff mode.
      This message is sent to the player when they try to use a command that is in the blockedCommands list.
      """)
    private String blockedCommandUsage = "You are not allowed to use this command while in staff mode.";

    @Setting
    @Comment("""
      A list of commands that are blocked when a player is in staff mode.
      This is useful for preventing staff members from using commands that could give them an unfair advantage.
      You can bypass this by giving the player the permission 'nookure.staff.mode.commands.bypass'.
      """)
    private List<String> blockedCommands = List.of("yourcommand1", "yourcommand2", "yourcommand3");

    public String getBlockedCommandUsage() {
        return blockedCommandUsage;
    }

    public List<String> getBlockedCommands() {
        return blockedCommands;
    }
}
