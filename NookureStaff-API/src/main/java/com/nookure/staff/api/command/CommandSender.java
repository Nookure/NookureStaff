package com.nookure.staff.api.command;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public interface CommandSender {
    /**
     * Send a message to the player.
     *
     * @param component the component to send
     */
    void sendMessage(@NotNull Component component);

    /**
     * Send a message to the player.
     * The message will be parsed with MiniMessage.
     *
     * @param message the message to send
     */
    default void sendMiniMessage(@NotNull String message, String... placeholders) {
        if (message.isBlank()) return;

        if (placeholders.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in pairs");
        }

        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
        }

        sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    /**
     * Send an actionbar to the player.
     *
     * @param component the component to send
     */
    void sendActionbar(@NotNull Component component);

    /**
     * Send a plain message to the player.
     *
     * @param message the plain message to send
     */
    default void sendPlainMessage(@NotNull String message) {
        sendMessage(Component.text(message));
    }

    /**
     * Gets the player's ping.
     *
     * @return the player's ping
     */
    int getPing();

    /**
     * Gets the player's display name.
     *
     * @return the player's display name
     */
    @NotNull Component getDisplayName();

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    @NotNull String getName();

    /**
     * Gets the player's unique id.
     *
     * @return the player's unique id
     */
    @NotNull UUID getUniqueId();

    /**
     * Check if the entity has the given permission.
     *
     * @param permission the permission to check
     * @return true if the entity has the given permission
     */
    boolean hasPermission(@NotNull String permission);

    /**
     * Check if the entity is a player.
     *
     * @return true if the entity is a player
     * @see #isConsole()
     */
    boolean isPlayer();

    /**
     * Check if the entity is the console.
     *
     * @return true if the entity is the console
     * @see #isPlayer()
     */
    default boolean isConsole() {
        return !isPlayer();
    }
}
