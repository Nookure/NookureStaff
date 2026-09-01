package com.nookure.staff.paper.brigadier;

import static java.util.Objects.requireNonNull;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BrigadierCommand extends Command {
    private final CommandSender consoleSender;
    private final PlayerWrapperManager<Player> playerWrapperManager;
    private final CommandDispatcher<CommandSender> dispatcher;

    protected BrigadierCommand(
            @NotNull final String name,
            @NotNull final CommandDispatcher<CommandSender> dispatcher,
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager,
            @NotNull final CommandSender consoleSender) {
        super(name);
        requireNonNull(dispatcher, "dispatcher cannot be null");
        requireNonNull(playerWrapperManager, "playerWrapperManager cannot be null");
        requireNonNull(consoleSender, "consoleSender cannot be null");
        this.dispatcher = dispatcher;
        this.playerWrapperManager = playerWrapperManager;
        this.consoleSender = consoleSender;
    }

    protected BrigadierCommand(
            @NotNull final String name,
            @NotNull final String description,
            @NotNull final String usageMessage,
            @NotNull final List<String> aliases,
            @NotNull final CommandDispatcher<CommandSender> dispatcher,
            @NotNull final PlayerWrapperManager<Player> playerWrapperManager,
            @NotNull final CommandSender consoleSender) {
        super(name, description, usageMessage, aliases);
        requireNonNull(dispatcher, "dispatcher cannot be null");
        requireNonNull(playerWrapperManager, "playerWrapperManager cannot be null");
        requireNonNull(consoleSender, "consoleSender cannot be null");
        this.dispatcher = dispatcher;
        this.playerWrapperManager = playerWrapperManager;
        this.consoleSender = consoleSender;
    }

    public void register() {
        dispatcher.register(build().createBuilder());
    }

    public abstract LiteralCommandNode<CommandSender> build();

    @Override
    public boolean execute(
            org.bukkit.command.@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            try {
                dispatcher.execute(commandLabel, consoleSender);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        Optional<PlayerWrapper> playerWrapperOptional = playerWrapperManager.getPlayerWrapper(player.getUniqueId());

        if (playerWrapperOptional.isEmpty()) {
            return false;
        }

        CommandSender commandSender = playerWrapperOptional.get();

        try {
            dispatcher.execute(commandLabel, commandSender);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
