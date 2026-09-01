package com.nookure.staff.api.item;

import com.nookure.staff.api.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item that executes an action when clicked.
 */
public interface ExecutableItem {
    /**
     * Called when the player clicks on the item.
     *
     * @param player the player that clicked on the item
     *               and will execute the action.
     */
    void click(@NotNull PlayerWrapper player);
}
