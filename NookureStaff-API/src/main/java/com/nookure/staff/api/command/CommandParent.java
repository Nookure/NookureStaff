package com.nookure.staff.api.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.nookure.staff.Constants;
import java.util.*;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a command that
 * have sub-commands.
 *
 * <p>
 * This class is abstract and must be extended.
 * The class must be annotated with {@link CommandData}.
 * </p>
 *
 * @see CommandData
 * @since 1.0.0
 */
public abstract class CommandParent extends Command {
    private final Map<String, Command> subCommands = new TreeMap<>();
    private final Map<String, CommandData> subCommandData = new TreeMap<>();
    private final ArrayList<String> subCommandNames = new ArrayList<>();

    @Inject
    private Injector injector;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.isEmpty()) {
            sendHelp(sender, label);
            return;
        }

        Optional<Command> optionalCommand = Optional.ofNullable(subCommands.get(args.get(0)));

        if (optionalCommand.isEmpty()) {
            sendHelp(sender, label);
            return;
        }

        Command command = optionalCommand.get();

        if (!sender.hasPermission(command.getCommandData().permission())) {
            sender.sendMiniMessage(getNoPermissionMessage());
            return;
        }

        command.onCommand(sender, label, args.subList(1, args.size()));
    }

    public void sendHelp(@NotNull CommandSender sender, @NotNull String label) {
        sender.sendMiniMessage("<b><red>Nookure</red></b><white>Staff</white> <gray>- <white>v" + Constants.VERSION);

        subCommandData.forEach((name, data) -> {
            sender.sendMiniMessage(
                    "<red><b>|</b> <white>/" + label + " " + data.name() + " <dark_gray>- " + data.description());
        });
    }

    @Override
    public void prepare() {
        Arrays.stream(getCommandData().subCommands()).forEach(subCommandClazz -> {
            Command subCommand = injector.getInstance(subCommandClazz);

            subCommands.put(subCommand.getCommandData().name(), subCommand);
            subCommandData.put(subCommand.getCommandData().name(), subCommand.getCommandData());

            if (subCommand.getCommandData().aliases() != null) {
                Arrays.stream(subCommand.getCommandData().aliases())
                        .forEach(alias -> subCommands.put(alias, subCommand));
            }
        });

        HelpCommand helpCommand = new HelpCommand(this);

        subCommands.put("help", helpCommand);
        subCommandData.put("help", helpCommand.getCommandData());

        subCommandNames.addAll(subCommands.keySet());
    }

    @Override
    public @NotNull List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        if (args.size() == 1) {
            return getSuggestionFilter(subCommandNames, args.getFirst());
        }

        Optional<Command> optionalCommand = Optional.ofNullable(subCommands.get(args.getFirst()));
        return optionalCommand
                .map(command -> command.onTabComplete(sender, label, args.subList(1, args.size())))
                .orElseGet(List::of);
    }

    public String getNoPermissionMessage() {
        return "<red>You don't have permission to execute this sub-command";
    }

    @CommandData(name = "help", description = "Shows the help message")
    private static final class HelpCommand extends Command {
        private final CommandParent parent;

        private HelpCommand(CommandParent parent) {
            this.parent = parent;
        }

        @Override
        public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
            parent.sendHelp(sender, label);
        }
    }

    public Map<String, Command> getSubCommands() {
        return subCommands;
    }

    public Map<String, CommandData> getSubCommandData() {
        return subCommandData;
    }
}
