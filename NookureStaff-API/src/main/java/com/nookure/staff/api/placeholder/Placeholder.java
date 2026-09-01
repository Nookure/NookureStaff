package com.nookure.staff.api.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Placeholder {
    /**
     * This method is called when a placeholder is requested
     *
     * @param player The player who requested the placeholder
     * @param params The parameters of the placeholder
     * @return The value of the placeholder
     */
    public abstract String onPlaceholderRequest(@Nullable Player player, @NotNull String params);
}
