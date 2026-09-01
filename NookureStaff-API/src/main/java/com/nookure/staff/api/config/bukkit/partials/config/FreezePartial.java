package com.nookure.staff.api.config.bukkit.partials.config;

import com.nookure.staff.api.util.NumberUtils;
import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class FreezePartial {

    @Setting
    @Comment("""
        Execute a command when the player is frozen and he or she
        exits of the servers.
      """)
    private boolean executeCommandOnExit = true;

    @Setting
    @Comment("""
        Ask the player to execute a command when he or she exits
        of the server.
      """)
    private boolean askToExecuteCommandOnExit = true;

    @Setting
    @Comment("""
      Enable or disable the chat freeze.
      This will prevent the player from sending messages in the chat
      and he will be able to chat with the staff members only.
      """)
    private boolean freezeChat = true;

    @Setting
    @Comment("""
      The value is with the following format: 1h 1m 1s
      Set "false" to disable the freeze timer.
      If the timer gets to 0, the player will be banned.
          """)
    private String freezeTimer = "5m";

    @Setting
    @Comment("""
      The commands to execute when the player is banned.
      You can use the following placeholders:
      ↳ {player} - Player name
      ↳ {staff} - Staff member name
      Use the proxy: prefix to execute a command in the proxy.
       """)
    private List<String> commands = List.of("ban {player} <red>You have been baned for ss evading");

    public boolean executeCommandOnExit() {
        return executeCommandOnExit;
    }

    public boolean askToExecuteCommandOnExit() {
        return askToExecuteCommandOnExit;
    }

    public boolean freezeChat() {
        return freezeChat;
    }

    public long freezeTimer() {
        if (freezeTimer.equalsIgnoreCase("false")) {
            return -1;
        }

        return NumberUtils.parseToMillis(freezeTimer);
    }

    public List<String> commands() {
        return commands;
    }
}
