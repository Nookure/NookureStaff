package com.nookure.staff.api.command;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a command.
 * <p>
 * This class is abstract and must be extended.
 * The class must be annotated with {@link CommandData}.
 * </p>
 *
 * @see CommandData
 * @since 1.0.0
 */
public abstract class Command {
    /**
     * This method is called when the command is executed
     *
     * @param sender The sender of the command
     * @param label  The label of the command
     * @param args   The arguments of the command
     */
    public abstract void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args);

    /**
     * This method is called when the command is tab completed
     *
     * @param sender The sender of the command
     * @param args   The arguments of the command
     * @return A list of possible completions
     */
    @NotNull public List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull String label, @NotNull List<String> args) {
        return List.of();
    }

    /**
     * This method is called when the command is registered
     * here you can prepare the command (e.g. register sub-commands)
     */
    public void prepare() {
        // Override this method to prepare the command
    }

    /**
     * Obtain a list of possible completions for the message
     *
     * @param args    The list of arguments
     * @param message The message to complete
     * @return A list of possible completions
     */
    public List<String> getSuggestionFilter(@NotNull List<String> args, @NotNull String message) {
        Objects.requireNonNull(args, "args can't be null");
        Objects.requireNonNull(message, "message can't be null");

        return args.stream()
                .filter(arg -> arg.toLowerCase().startsWith(message.toLowerCase()))
                .toList();
    }

    /**
     * Get the command data of the command
     *
     * @return the command data of the command
     */
    @NotNull public CommandData getCommandData() {
        CommandData commandData = getClass().getAnnotation(CommandData.class);

        if (commandData == null) {
            throw new IllegalStateException("CommandData annotation not found");
        }

        return commandData;
    }
}
