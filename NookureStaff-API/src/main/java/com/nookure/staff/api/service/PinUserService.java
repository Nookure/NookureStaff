package com.nookure.staff.api.service;

import com.nookure.staff.api.model.PlayerModel;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * This service is responsible for handling the pin user server.
 */
public interface PinUserService {
    /**
     * Check if the pin is valid for the player.
     *
     * @param uuid the player to check
     * @param pin  the pin to check
     * @return true if the pin is valid, false otherwise
     */
    boolean isValid(@NotNull UUID uuid, @NotNull String pin);

    /**
     * Check if the pin is valid for the player.
     *
     * @param player the player to check
     * @param pin    the pin to check
     * @return true if the pin is valid, false otherwise
     */
    boolean isValid(@NotNull PlayerModel player, @NotNull String pin);

    /**
     * Set the pin for the player.
     *
     * @param player the player to set the pin
     * @param pin    the pin to set
     */
    void setPin(@NotNull PlayerModel player, @NotNull String pin);

    /**
     * Check if the player has a pin set.
     *
     * @param player the player to check
     * @return true if the player has a pin set, false otherwise
     */
    boolean isPinSet(@NotNull PlayerModel player);

    void updateIp(@NotNull PlayerModel player);
}
