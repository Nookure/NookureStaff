package com.nookure.staff.api.config.bukkit.partials.messages;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class StaffModePartial {
    @Setting
    @Comment("Message sent to the player when the staff mode is toggled on.")
    private String toggledOn = "{prefix} <gray>Staff mode has been toggled <green>on</green>.";

    @Setting
    @Comment("Message sent to the player when the staff mode is toggled off.")
    private String toggledOff = "{prefix} <gray>Staff mode has been toggled <red>off</red>.";

    @Setting
    @Comment("Message sent to others when a player toggles staff mode on.")
    private String toggledOnOthers = "{prefix} <red>{player}</red> <gray>has toggled staff mode <green>on</green>.";

    @Setting
    @Comment("Message sent to others when a player toggles staff mode off.")
    private String toggledOffOthers = "{prefix} <red>{player}</red> <gray>has toggled staff mode <red>off</red>.";

    @Setting
    @Comment("Message sent to the player when there are no players online.")
    private String noPlayersOnline = "{prefix} <red>There are no players online.";

    @Setting
    @Comment("Message sent to the player when teleporting to another player.")
    private String teleportingTo = "{prefix} <gray>Teleporting to <red>{player}</red>.";

    @Setting
    @Comment("""
      Action bar format for the staff mode.
      """)
    private String actionBar =
            "<red>Vanished <gray>(</gray> {vanished} <gray>)</gray> <red>StaffChat <gray>(</gray> {staffChat} <gray>)</gray> <red>TPS: <red>{tps}";

    @Setting
    @Comment("Message sent to the player when the invsee command is used incorrectly.")
    private String invseeCommandUsage = "{prefix} <gray>Usage: <red>/staff invsee <player>";

    @Setting
    @Comment("Message sent to the player when the invsee enderchest command is used incorrectly.")
    private String invseeEnderchestCommandUsage = "{prefix} <gray>Usage: <red>/staff enderchest <player>";

    public String toggledOn() {
        return toggledOn;
    }

    public String toggledOff() {
        return toggledOff;
    }

    public String noPlayersOnline() {
        return noPlayersOnline;
    }

    public String teleportingTo() {
        return teleportingTo;
    }

    public String toggledOnOthers() {
        return toggledOnOthers;
    }

    public String toggledOffOthers() {
        return toggledOffOthers;
    }

    public String actionBar() {
        return actionBar;
    }

    public String invseeCommandUsage() {
        return invseeCommandUsage;
    }

    public String invseeEnderchestCommandUsage() {
        return invseeEnderchestCommandUsage;
    }
}
