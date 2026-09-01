package com.nookure.staff.paper.factory;

import com.nookure.staff.api.state.PlayerState;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface StaffPaperPlayerWrapperFactory {
    /**
     * Create a new player wrapper
     *
     * @param player      The player to wrap
     * @return The new staff player wrapper
     */
    @NotNull StaffPaperPlayerWrapper create(
            @NotNull final Player player, @NotNull final List<Class<? extends PlayerState>> states);
}
