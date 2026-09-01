package com.nookure.staff.api.manager;

import com.google.inject.Singleton;
import com.nookure.staff.api.item.StaffItem;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Manages all staff items.
 *
 * @see StaffItem
 * @see com.nookure.staff.api.config.bukkit.partials.ItemPartial
 * @since 1.0.0
 */
@Singleton
public final class StaffItemsManager {
    private final Map<String, StaffItem> items = new HashMap<>();

    /**
     * Get a staff item by its identifier.
     *
     * @param identifier the identifier of the staff item.
     * @param item       the staff item.
     */
    public void addItem(@NotNull String identifier, @NotNull StaffItem item) {
        Objects.requireNonNull(identifier, "Identifier cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        items.put(identifier, item);
    }

    /**
     * Get a staff item by its identifier.
     *
     * @param identifier the identifier of the staff item.
     * @return the staff item.
     */
    public StaffItem getItem(@NotNull String identifier) {
        Objects.requireNonNull(identifier, "Identifier cannot be null");

        return items.get(identifier);
    }

    /**
     * Get all staff items.
     *
     * @return all staff items.
     */
    public Map<String, StaffItem> getItems() {
        return items;
    }

    /**
     * Remove a staff item by its identifier.
     *
     * @param identifier the identifier of the staff item.
     */
    public void removeItem(@NotNull String identifier) {
        Objects.requireNonNull(identifier, "Identifier cannot be null");

        items.remove(identifier);
    }

    /**
     * Remove all staff items.
     */
    public void clearItems() {
        items.clear();
    }
}
