package com.nookure.staff.paper.command;

import com.google.inject.Inject;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.command.CommandData;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.command.sender.ConsoleCommandSender;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TemplateCommand extends Command {
    private final com.nookure.staff.api.command.Command command;

    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConsoleCommandSender consoleCommandSender;

    protected TemplateCommand(@NotNull com.nookure.staff.api.command.Command command) {

        super(command.getCommandData().name());

        this.command = command;

        CommandData data = command.getCommandData();

        if (data.aliases() != null) {
            setAliases(Arrays.stream(data.aliases()).toList());
        }

        if (data.permission() != null) {
            setPermission(data.permission());
        }

        if (data.usage() != null) {
            setUsage(data.usage());
        }

        if (data.description() != null) {
            setDescription(data.description());
        }
    }

    @Override
    public boolean execute(
            @NotNull org.bukkit.command.CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player bukkitPlayer) {
            playerWrapperManager
                    .getPlayerWrapper(bukkitPlayer.getUniqueId())
                    .ifPresent(player -> command.onCommand(player, commandLabel, List.of(args)));
        } else {
            command.onCommand(consoleCommandSender, commandLabel, List.of(args));
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args)
            throws IllegalArgumentException {
        if (sender instanceof Player bukkitPlayer) {
            Optional<PlayerWrapper> playerWrapper = playerWrapperManager.getPlayerWrapper(bukkitPlayer.getUniqueId());

            return playerWrapper
                    .map(wrapper -> command.onTabComplete(wrapper, alias, List.of(args)))
                    .orElseGet(List::of);
        } else {
            return command.onTabComplete(consoleCommandSender, alias, List.of(args));
        }
    }
}
