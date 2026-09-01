package com.nookure.staff.paper.factory;

import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.paper.PaperPlayerWrapper;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PaperPlayerWrapperFactory {
    /**
     * Create a new player wrapper
     *
     * @param player The player to wrap
     * @param states The states to add to the player
     * @return The new player wrapper
     */
    @NotNull PaperPlayerWrapper create(@NotNull final Player player, @NotNull final List<Class<? extends PlayerState>> states);
}
