package com.nookure.staff.api;

import com.nookure.staff.api.command.CommandSender;
import com.nookure.staff.api.model.PlayerModel;
import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.api.state.WrapperState;
import java.io.Serializable;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface PlayerWrapper extends Serializable, CommandSender {
    /**
     * Send a plugin message to the proxy.
     *
     * @param channel the channel to send the message to
     * @param message the message to send
     */
    void sendPluginMessage(@NotNull String channel, byte @NotNull [] message);

    /**
     * Gets a set containing all the plugin channels
     * that this player is listening to.
     *
     * @return a set containing all the plugin channels
     */
    @NotNull Set<String> getListeningPluginChannels();

    /**
     * Teleport the player to another player.
     *
     * @param to the player to teleport to
     */
    void teleport(@NotNull PlayerWrapper to);

    /**
     * Kick the player from the server.
     *
     * @param reason the reason for the kick
     */
    void kick(@NotNull String reason);

    /**
     * Kick the player from the server.
     *
     * @param reason the reason for the kick
     */
    void kick(@NotNull Component reason);

    /**
     * Get the player's model.
     *
     * @return the player's model
     * @throws IllegalStateException if the player's model feature is disabled
     */
    PlayerModel getPlayerModel();

    /**
     * Get the player's state.
     *
     * @return the player's state
     */
    @NotNull WrapperState getState();

    /**
     * Check if the player has a state.
     *
     * @param clazz the class of the state
     */
    default boolean hasState(Class<? extends PlayerState> clazz) {
        return getState().hasState(clazz);
    }
}
