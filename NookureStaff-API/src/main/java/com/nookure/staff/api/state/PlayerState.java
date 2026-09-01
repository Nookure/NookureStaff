package com.nookure.staff.api.state;

import static java.util.Objects.requireNonNull;

import com.nookure.staff.api.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerState {
    private final PlayerWrapper wrapper;

    /**
     * Constructs a new PlayerState.
     *
     * @param wrapper the player wrapper
     */
    public PlayerState(@NotNull PlayerWrapper wrapper) {
        requireNonNull(wrapper, "PlayerWrapper cannot be null");
        this.wrapper = wrapper;
    }

    /**
     * Gets the player wrapper.
     *
     * @return the player wrapper
     */
    public PlayerWrapper getWrapper() {
        return wrapper;
    }
}
