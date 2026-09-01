package com.nookure.staff.api.config.bukkit;

import com.nookure.staff.api.config.bukkit.partials.messages.*;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class BukkitMessages {
    public StaffModePartial staffMode = new StaffModePartial();
    public VanishPartial vanish = new VanishPartial();
    public FreezeMessagePartial freeze = new FreezeMessagePartial();
    public StaffChatPartial staffChat = new StaffChatPartial();
    public PlaceholderPartial placeholder = new PlaceholderPartial();
    public PinMessagePartial pin = new PinMessagePartial();

    @Setting
    @Comment("""
          The prefix for all staff messages.
          If you want to use the prefix in a message, use {prefix}.
          """)
    private String prefix = "<b><red>Staff</red> <gray>»</gray></b>";

    @Setting
    @Comment("""
          Reload message
          """)
    private String reload =
            "{prefix} <gray>Configuration reloaded, mayor changes may not take effect until the server is restarted.";

    @Setting
    @Comment("""
          Player not found message
          """)
    private String playerNotFound = "{prefix} <red>Could not find the player {player}.";

    @Setting
    @Comment("""
      The message when you don't have a permission
      """)
    private String noPermission = "{prefix} <red>You don't have permission to do that.";

    @Setting
    @Comment("The message when a player has been registered as a staff member during the command execution.")
    private String youAreNowAnStaffDuringCommandExecution =
            "{prefix} <green>You have been just registered as a staff member, please redo the command in order to use it.";

    @Setting
    @Comment("The message when a player has been registered as a staff member during the permission interceptor.")
    private String youAreNowAnStaffDuringPermissionInterceptor =
            "{prefix} <green>Congratulations!, welcome to the Staff members.";

    @Setting
    @Comment("The message when only staff members can execute a command.")
    private String onlyStaffMembersCanExecuteThisCommand = "{prefix} <red>Only staff members can execute this command.";

    public String prefix() {
        return prefix;
    }

    public String reload() {
        return reload;
    }

    public String playerNotFound() {
        return playerNotFound;
    }

    public String noPermission() {
        return noPermission;
    }

    public String youAreNowAnStaff() {
        return youAreNowAnStaffDuringCommandExecution;
    }

    public String youAreNowAnStaffDuringPermissionInterceptor() {
        return youAreNowAnStaffDuringPermissionInterceptor;
    }

    public String onlyStaffMembersCanExecuteThisCommand() {
        return onlyStaffMembersCanExecuteThisCommand;
    }
}
