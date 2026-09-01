package com.nookure.staff.api.util.transformer;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerTransformer {
    /**
     * Transform a player to a staff member
     *
     * @param uuid The player's UUID
     */
    void player2Staff(@NotNull final UUID uuid);

    /**
     * Transform a staff member to a player
     *
     * @param uuid The player's UUID
     */
    void staff2player(@NotNull final UUID uuid);
}
