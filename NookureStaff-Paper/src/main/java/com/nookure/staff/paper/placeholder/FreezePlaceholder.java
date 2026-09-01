package com.nookure.staff.paper.placeholder;

import com.google.inject.Inject;
import com.nookure.staff.api.config.ConfigurationContainer;
import com.nookure.staff.api.config.bukkit.BukkitMessages;
import com.nookure.staff.api.manager.FreezeManager;
import com.nookure.staff.api.placeholder.Placeholder;
import com.nookure.staff.api.placeholder.PlaceholderData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PlaceholderData("freeze")
public class FreezePlaceholder extends Placeholder {
    @Inject
    private FreezeManager freezeManager;

    @Inject
    private ConfigurationContainer<BukkitMessages> messages;

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        if (player == null) return messages.get().placeholder.placeholderFalse();

        return freezeManager.isFrozen(player.getUniqueId())
                ? messages.get().placeholder.placeholderTrue()
                : messages.get().placeholder.placeholderFalse();
    }
}
