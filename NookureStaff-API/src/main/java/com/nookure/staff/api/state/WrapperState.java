package com.nookure.staff.api.state;

import java.util.HashMap;

/**
 * Represents the state of a wrapper (staff/player).
 */
public final class WrapperState {
    private final HashMap<Class<? extends PlayerState>, PlayerState> states = new HashMap<>();

    /**
     * Sets the state of the wrapper.
     *
     * @param clazz the class of the state
     * @param state the state
     */
    public <T extends PlayerState> void setState(Class<T> clazz, T state) {
        states.put(clazz, state);
    }

    /**
     * Sets the state of the wrapper.
     *
     * @param state the state
     */
    public void setState(PlayerState state) {
        states.put(state.getClass(), state);
    }

    /**
     * Gets the state of the wrapper.
     *
     * @param clazz the class of the state
     * @return the state
     */
    public <T extends PlayerState> T getState(Class<T> clazz) {
        return clazz.cast(states.get(clazz));
    }

    /**
     * Removes the state of the wrapper.
     *
     * @param clazz the class of the state
     */
    public <T> void removeState(Class<T> clazz) {
        states.remove(clazz);
    }

    /**
     * Checks if the wrapper has a state.
     *
     * @param clazz the class of the state
     * @return true if the wrapper has the state, false otherwise
     */
    public boolean hasState(Class<? extends PlayerState> clazz) {
        return states.containsKey(clazz);
    }
}
