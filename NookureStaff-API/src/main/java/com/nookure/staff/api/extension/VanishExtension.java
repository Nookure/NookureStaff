package com.nookure.staff.api.extension;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an extension for the vanish feature
 * of the player
 */
public abstract class VanishExtension extends StaffPlayerExtension {
    /**
     * Create a new extension for the player
     *
     * @param player the player
     */
    @Inject
    public VanishExtension(@NotNull final StaffPlayerWrapper player) {
        super(player);
    }

    /**
     * Enable vanish for the player
     *
     * @param silent if true the server will not send a message
     *               to the player that this player has left the server
     */
    public abstract void enableVanish(boolean silent);

    /**
     * Disable vanish for the player
     *
     * @param silent if true the server will not send a message
     *               to the player that this player has joined the server
     */
    public abstract void disableVanish(boolean silent);

    /**
     * Check if the player is vanished
     *
     * @return true if the player is vanished
     */
    public abstract boolean isVanished();

    /**
     * Set the vanished state of the player
     *
     * @param vanished true if the player should be vanished
     */
    public abstract void setVanished(boolean vanished);

    /**
     * Whether the player state should be restored from the Nookure Staff
     * database
     *
     * @return true if the player state should be restored from the database
     */
    public boolean restoreFromDatabase() {
        return true;
    }
}
