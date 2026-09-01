package com.nookure.staff.api.util;

/**
 * Simple abstract class to stream instances and load them.
 * This class should have access to the injector.
 *
 * @since 1.0.0
 */
public interface AbstractLoader {
    /**
     * load the instance.
     */
    void load();

    /**
     * Reloads the feature
     */
    default void reload() {}

    /**
     * Unloads the feature
     */
    default void unload() {}
}
