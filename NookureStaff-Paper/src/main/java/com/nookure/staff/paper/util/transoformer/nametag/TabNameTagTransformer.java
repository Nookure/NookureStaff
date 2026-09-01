package com.nookure.staff.paper.util.transoformer.nametag;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.util.transformer.NameTagTransformer;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class TabNameTagTransformer implements NameTagTransformer {
    @Inject
    public TabNameTagTransformer(@NotNull final Logger logger) {
        if (getNameTagManager() == null) {
            logger.severe("TabAPI is not loaded, disabling TabNametagTransformer.");
        }
    }

    @Override
    public void setPrefix(@NotNull PlayerWrapper player, @NotNull String prefix) {
        requireNonNull(player, "Player cannot be null.");
        requireNonNull(prefix, "Prefix cannot be null.");
        final var nameTagManager = getNameTagManager();

        if (nameTagManager == null) return;
        nameTagManager.setPrefix(getTabPlayer(player), prefix);
    }

    @Override
    public void removePrefix(@NotNull PlayerWrapper player) {
        requireNonNull(player, "Player cannot be null.");
        final var nameTagManager = getNameTagManager();

        if (nameTagManager == null) return;
        nameTagManager.setPrefix(getTabPlayer(player), null);
    }

    @Override
    public void setSuffix(@NotNull PlayerWrapper player, @NotNull String suffix) {
        requireNonNull(player, "Player cannot be null.");
        final var nameTagManager = getNameTagManager();

        if (nameTagManager == null) return;
        nameTagManager.setSuffix(getTabPlayer(player), suffix);
    }

    @Override
    public void removeSuffix(@NotNull PlayerWrapper player) {
        requireNonNull(player, "Player cannot be null.");
        final var nameTagManager = getNameTagManager();

        if (nameTagManager == null) return;
        nameTagManager.setSuffix(getTabPlayer(player), null);
    }

    private TabPlayer getTabPlayer(PlayerWrapper player) {
        return TabAPI.getInstance().getPlayer(player.getUniqueId());
    }

    private NameTagManager getNameTagManager() {
        return TabAPI.getInstance().getNameTagManager();
    }
}
