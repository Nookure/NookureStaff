package com.nookure.staff.api.config.bukkit.partials;

import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CustomItemPartial extends ItemPartial {
    @Setting
    @Comment("""
      The type of the custom item.
      - COMMAND_AS_PLAYER: The command will be executed as the player who clicked the item.
      - COMMAND_AS_CONSOLE: The command will be executed as the console.
      - COMMAND_TARGET_AS_PLAYER: The command will be executed
          as the player who is staff but with the placeholder of {target} replaced by the player that
          have been clicked.
      - COMMAND_TARGET_AS_CONSOLE: The command will be executed as the console but with the placeholder
          of {target} replaced by the player that have been clicked.
      """)
    private CustomItemType type;

    @Setting
    @Comment("""
      The command to execute when the item is clicked.
      You can use the placeholder {player} to get the player who clicked the item.
      You can use the placeholder {target} to get the player who have been clicked.
      """)
    private String command;

    public CustomItemPartial() {}

    public CustomItemPartial(
            final boolean enabled,
            @NotNull final String name,
            @NotNull final Material material,
            final int slot,
            @NotNull final List<String> lore,
            @NotNull final String permission,
            @NotNull final CustomItemType type,
            @NotNull final String command) {
        super(enabled, name, material, slot, lore, permission);
        this.type = type;
        this.command = command;
    }

    public CustomItemType getType() {
        return type;
    }

    public void setType(CustomItemType type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
