package com.nookure.staff.paper.placeholder;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.placeholder.Placeholder;
import com.nookure.staff.api.placeholder.PlaceholderData;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PlaceholderData("vanish")
public class VanishPlaceholder extends Placeholder {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        if (player == null) return messages.get().placeholder.placeholderFalse();

        Optional<StaffPlayerWrapper> staffPlayerWrapper = playerWrapperManager.getStaffPlayer(player.getUniqueId());

        if (staffPlayerWrapper.isEmpty()) return messages.get().placeholder.placeholderFalse();

        return staffPlayerWrapper.get().isInVanish()
                ? messages.get().placeholder.placeholderTrue()
                : messages.get().placeholder.placeholderFalse();
    }
}
