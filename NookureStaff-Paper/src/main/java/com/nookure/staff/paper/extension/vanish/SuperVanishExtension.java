package com.nookure.staff.paper.extension.vanish;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.extension.VanishExtension;
import com.nookure.staff.paper.StaffPaperPlayerWrapper;
import de.myzelyam.api.vanish.VanishAPI;
import org.jetbrains.annotations.NotNull;

public class SuperVanishExtension extends VanishExtension {
    private final StaffPaperPlayerWrapper player;

    @Inject
    public SuperVanishExtension(@NotNull final StaffPlayerWrapper player) {
        super(player);
        this.player = (StaffPaperPlayerWrapper) player;
    }

    @Override
    public void enableVanish(boolean silent) {
        if (!isVanished()) VanishAPI.hidePlayer(player.getPlayer());
    }

    @Override
    public void disableVanish(boolean silent) {
        if (isVanished()) VanishAPI.showPlayer(player.getPlayer());
    }

    @Override
    public boolean isVanished() {
        return VanishAPI.isInvisible(player.getPlayer());
    }

    @Override
    public void setVanished(boolean vanished) {}

    @Override
    public boolean restoreFromDatabase() {
        return false;
    }
}
