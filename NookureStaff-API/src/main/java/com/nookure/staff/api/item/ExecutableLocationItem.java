package com.nookure.staff.api.item;

import com.nookure.staff.api.PlayerWrapper;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item that executes an action when clicked.
 * This item will also include the location where the player clicked.
 */
public interface ExecutableLocationItem {
    /**
     * Called when the player clicks on the item.
     *
     * @param player   the player that clicked on the item
     * @param location the location where the player clicked
     */
    void click(@NotNull PlayerWrapper player, @NotNull Location location);
}
