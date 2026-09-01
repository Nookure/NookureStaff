package com.nookure.staff.paper.placeholder;

import com.google.inject.Inject;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.placeholder.Placeholder;
import com.nookure.staff.api.placeholder.PlaceholderData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PlaceholderData("staff_count")
public class StaffCountPlaceholder extends Placeholder {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        return String.valueOf(playerWrapperManager.getStaffCount());
    }
}
