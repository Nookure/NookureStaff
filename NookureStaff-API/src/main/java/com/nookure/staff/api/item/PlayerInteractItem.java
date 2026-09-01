package com.nookure.staff.api.item;

import com.nookure.staff.api.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item that executes an action when clicked on other player.
 * This item will also include the player that was clicked.
 */
public interface PlayerInteractItem {
    /**
     * Called when the player clicks on the item.
     *
     * @param player the player that clicked on the item
     * @param target the player that was clicked
     */
    void click(@NotNull PlayerWrapper player, @NotNull PlayerWrapper target);
}
