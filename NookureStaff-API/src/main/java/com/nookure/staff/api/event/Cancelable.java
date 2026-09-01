package com.nookure.staff.api.event;

/**
 * Represents an event that can be cancelled.
 *
 * @see Event
 */
public interface Cancelable {
    /**
     * Gets if the event is cancelled.
     *
     * @return If the event is cancelled
     */
    boolean isCancelled();

    /**
     * Sets if the event is cancelled.
     *
     * @param cancelled If the event is cancelled
     */
    void setCancelled(boolean cancelled);
}
