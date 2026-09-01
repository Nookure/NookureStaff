package com.nookure.staff.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface Logger {
    /**
     * Log a message to the console
     *
     * @param message The message to log
     */
    default void info(String message) {
        info(MiniMessage.miniMessage().deserialize("<white>" + message));
    }

    /**
     * Log a warning to the console
     *
     * @param message The message to log
     */
    default void warning(String message) {
        warning(Component.text(message).color(NamedTextColor.YELLOW));
    }

    /**
     * Log a severe error to the console
     *
     * @param message The message to log
     */
    default void severe(String message) {
        severe(Component.text(message).color(NamedTextColor.RED));
    }

    /**
     * Log a severe error to the console
     *
     * @param throwable The throwable to log
     */
    default void severe(Throwable throwable) {
        severe(throwable.getMessage());
        throwable.printStackTrace(System.err);
    }

    /**
     * Log a debug message to the console
     *
     * @param message The message to log
     */
    default void debug(String message) {
        debug(Component.text(message).color(NamedTextColor.GRAY));
    }

    default void info(String message, Object... args) {
        info(String.format(message, args));
    }

    default void warning(String message, Object... args) {
        warning(String.format(message, args));
    }

    default void severe(String message, Object... args) {
        severe(String.format(message, args));
    }

    default void debug(String message, Object... args) {
        debug(String.format(message, args));
    }

    /**
     * Log a message to the console
     *
     * @param component The component to log
     * @see net.kyori.adventure.text.Component
     */
    void info(Component component);

    /**
     * Log a warning to the console
     *
     * @param component The component to log
     * @see net.kyori.adventure.text.Component
     */
    void warning(Component component);

    /**
     * Log a severe error to the console
     *
     * @param component The component to log
     * @see net.kyori.adventure.text.Component
     */
    void severe(Component component);

    /**
     * Log a debug message to the console
     *
     * @param component The component to log
     * @see net.kyori.adventure.text.Component
     */
    void debug(Component component);
}
