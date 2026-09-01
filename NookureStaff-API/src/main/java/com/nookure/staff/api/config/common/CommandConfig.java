package com.nookure.staff.api.config.common;

import com.nookure.staff.api.Permissions;
import java.util.HashMap;
import java.util.List;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandConfig {
    private static final HashMap<String, CommandPartial> DEFAULT;

    static {
        DEFAULT = new HashMap<>();

        DEFAULT.put(
                "vanish",
                new CommandPartial(
                        "vanish",
                        List.of(),
                        Permissions.STAFF_VANISH,
                        "Vanish command",
                        "Toggle your vanish status",
                        true));
    }

    @Setting
    private HashMap<String, CommandPartial> commands = new HashMap<>(DEFAULT);

    public HashMap<String, CommandPartial> getCommands() {
        return commands;
    }

    public void addCommand(String name, CommandPartial command) {
        commands.put(name, command);
    }
}
