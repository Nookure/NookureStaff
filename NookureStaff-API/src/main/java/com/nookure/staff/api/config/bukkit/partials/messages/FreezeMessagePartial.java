package com.nookure.staff.api.config.bukkit.partials.messages;

import static com.nookure.staff.api.Permissions.STAFF_FREEZE_BYPASS;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class FreezeMessagePartial {
    @Setting
    @Comment("The message to send to the player when he or she is frozen.")
    private String frozenMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>You have been <green>frozen</green>.</gray>";

    @Setting
    @Comment("The message to send to the player when he or she is unfrozen.")
    private String unfrozenMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>You have been <green>unfrozen</green>.</gray>";

    @Setting
    @Comment("The message to send to the staff member when the player is frozen.")
    private String staffFrozenMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>{player} has been <green>frozen</green> by <green>{staff}</green>.</gray>";

    @Setting
    @Comment("The message to send to the staff member when the player is unfrozen.")
    private String staffUnfrozenMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>{player} has been <green>unfrozen</green> by <green>{staff}</green>.</gray>";

    @Setting
    @Comment("The message to repeat in the chat when the player is frozen.")
    private String chatFrozenMessage = """
          &7████&c█&7████
          &7███&c█&6█&c█&7███
          &7██&c█&6█&0█&6█&c█&7██
          &7██&c█&6█&0█&6█&c█&7██   You have been frozen!
          &7█&c█&6██&0█&6██&c█&7█  Join our discord server:
          &7█&c█&6█████&c█&7█  http:/discord.example.com
          &c█&6███&0█&6███&c█  You have {time} to join.
          &c█████████
      """;

    @Setting
    @Comment("""
      This message will be sent to the staff when the player who he froze leaves the server.
      """)
    private String confirmPunishMessage = """
      <gray>The <green>player</green> <red>{player}</red> has left the server while he was freezed
      Do you want to execute the commands specified when a player leaves ? <hover:show_text:'Forgive the player'><red><click:run_command:'/freeze /remove {uuid}'>[x]</click></red></hover> <click:run_command:/freeze /exec {uuid}><green><hover:show_text:'Punish that player'>[✔]</hover></green> </click>
      """;

    @Setting
    @Comment("Message sent when a staff tries to freeze a player with " + STAFF_FREEZE_BYPASS + " permission.")
    private String freezeBypassMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>You can't freeze that player.</gray>";

    @Setting
    @Comment("The usage message for the /freeze command.")
    private String freezeCommandUsage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <red>You must use /freeze {player} to freeze a player.</red>";

    @Setting
    @Comment("The format of the chat message when the player is frozen.")
    private String freezeChatFormat = "{player} <dark_gray>(frozen)</dark_gray> » <gray>{message}</gray>";

    @Setting
    @Comment("Chat format for staff members that are talking to frozen players.")
    private String freezeStaffChatFormat =
            "{player} <dark_gray>(staff)</dark_gray> -> {target} <dark_gray>(frozen)</dark_gray> » <gray>{message}</gray>";

    @Setting
    @Comment("FreezeChat command usage message.")
    private String freezeChatUsage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <red>You must use /freezechat {player} <message> to chat with a frozen player.</red>";

    @Setting
    @Comment("Freeze Chat when a player isn't frozen and you try to use it.")
    private String notFrozenChat =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <red>You can't use the freeze chat if the player isn't frozen.</red>";

    @Setting
    @Comment("Freeze Chat when a player isn't online and you try to use it.")
    private String playerNotOnline =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <red>That player is not online.</red>";

    @Setting
    @Comment("Message sent when a player is forgiven")
    private String forgiveMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>You have forgiven {player}.</gray>";

    @Setting
    @Comment("Message sent when a player is punished")
    private String punishMessage =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>You have punished {player}.</gray>";

    @Setting
    @Comment("Message sent when the timer has been paused for a player")
    private String theTimerHasBeenPausedFor =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>The timer has been paused for {player}.</gray>";

    @Setting
    @Comment("Message sent when the staff has paused the timer for a player")
    private String theStaffHasPausedTheTimer =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <gray>The staff has paused the timer for you</gray>";

    @Setting
    @Comment("Message sent when a player is not frozen.")
    private String playerNotFrozen =
            "<bold><dark_aqua>Freeze <dark_gray>»</bold> <red>That player is not frozen.</red>";

    public String playerNotFrozen() {
        return playerNotFrozen;
    }

    public String frozenMessage() {
        return frozenMessage;
    }

    public String unfrozenMessage() {
        return unfrozenMessage;
    }

    public String staffFrozenMessage() {
        return staffFrozenMessage;
    }

    public String staffUnfrozenMessage() {
        return staffUnfrozenMessage;
    }

    public String chatFrozenMessage() {
        return chatFrozenMessage;
    }

    public String confirmPunishMessage() {
        return confirmPunishMessage;
    }

    public String freezeBypassMessage() {
        return freezeBypassMessage;
    }

    public String freezeChatFormat() {
        return freezeChatFormat;
    }

    public String freezeStaffChatFormat() {
        return freezeStaffChatFormat;
    }

    public String freezeChatUsage() {
        return freezeChatUsage;
    }

    public String notFrozenChat() {
        return notFrozenChat;
    }

    public String freezeCommandUsage() {
        return freezeCommandUsage;
    }

    public String playerNotOnline() {
        return playerNotOnline;
    }

    public String forgiveMessage() {
        return forgiveMessage;
    }

    public String punishMessage() {
        return punishMessage;
    }

    public String theTimerHasBeenPausedFor() {
        return theTimerHasBeenPausedFor;
    }

    public String theStaffHasPausedTheTimer() {
        return theStaffHasPausedTheTimer;
    }
}
