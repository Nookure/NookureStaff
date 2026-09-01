package com.nookure.staff.api.command;

import org.jetbrains.annotations.NotNull;

/**
 * This class is used to manage commands.
 * It is used to register and unregister commands.
 *
 * @see com.nookure.staff.api.command.Command
 * @since 1.0.0
 */
public abstract class CommandManager {
    /**
     * Register a command
     *
     * @param command The command to register
     */
    public abstract void registerCommand(@NotNull Command command);

    /**
     * Unregister a command
     *
     * @param command The command to unregister
     */
    public abstract void unregisterCommand(@NotNull Command command);
}
