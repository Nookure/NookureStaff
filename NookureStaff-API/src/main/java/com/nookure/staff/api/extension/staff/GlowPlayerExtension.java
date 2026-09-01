package com.nookure.staff.api.extension.staff;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.extension.StaffPlayerExtension;
import org.jetbrains.annotations.NotNull;

public abstract class GlowPlayerExtension extends StaffPlayerExtension {
    @Inject
    public GlowPlayerExtension(@NotNull StaffPlayerWrapper player) {
        super(player);
    }

    @NotNull public abstract String getGlowColor();
}
