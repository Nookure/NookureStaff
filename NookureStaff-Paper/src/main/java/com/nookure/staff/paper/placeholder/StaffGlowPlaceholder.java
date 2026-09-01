package com.nookure.staff.paper.placeholder;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.extension.staff.GlowPlayerExtension;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import com.nookure.staff.api.placeholder.Placeholder;
import com.nookure.staff.api.placeholder.PlaceholderData;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PlaceholderData("glow_color")
public class StaffGlowPlaceholder extends Placeholder {
    @Inject
    private PlayerWrapperManager<Player> playerWrapperManager;

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        if (player == null) return "";

        Optional<StaffPlayerWrapper> staffPaperPlayerWrapperOptional =
                playerWrapperManager.getStaffPlayer(player.getUniqueId());

        if (staffPaperPlayerWrapperOptional.isEmpty()) return "";

        var glowOptional = staffPaperPlayerWrapperOptional.get().getExtension(GlowPlayerExtension.class);

        return glowOptional.map(GlowPlayerExtension::getGlowColor).orElse("");
    }
}
