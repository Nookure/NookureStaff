package com.nookure.staff.api.placeholder;

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class PlaceholderManager {
    private final HashMap<String, Placeholder> placeholderRegistry = new HashMap<>();

    /**
     * Register a placeholder
     *
     * @param placeholder The placeholder to register
     */
    public void registerPlaceholder(@NotNull Placeholder placeholder) {
        Objects.requireNonNull(placeholder, "Placeholder cannot be null");

        PlaceholderData placeholderData = placeholder.getClass().getAnnotation(PlaceholderData.class);

        if (placeholderData == null) {
            throw new IllegalStateException("PlaceholderData annotation not found");
        }

        synchronized (placeholderRegistry) {
            placeholderRegistry.put(placeholderData.value(), placeholder);
        }
    }

    /**
     * Unregister a placeholder
     *
     * @param placeholder The placeholder to unregister
     */
    public void unregisterPlaceholder(@NotNull Placeholder placeholder) {
        Objects.requireNonNull(placeholder, "Placeholder cannot be null");

        PlaceholderData placeholderData = placeholder.getClass().getAnnotation(PlaceholderData.class);

        if (placeholderData == null) {
            throw new IllegalStateException("PlaceholderData annotation not found");
        }

        synchronized (placeholderRegistry) {
            placeholderRegistry.remove(placeholderData.value());
        }
    }

    /**
     * Get a placeholder by key
     *
     * @param key The key of the placeholder
     * @return The placeholder if found
     */
    public Optional<Placeholder> getPlaceholder(@NotNull String key) {
        Objects.requireNonNull(key, "Key cannot be null");

        synchronized (placeholderRegistry) {
            return Optional.ofNullable(placeholderRegistry.get(key));
        }
    }

    /**
     * Get the placeholder registry
     *
     * @return The placeholder registry
     */
    public HashMap<String, Placeholder> getPlaceholderRegistry() {
        return placeholderRegistry;
    }
}
