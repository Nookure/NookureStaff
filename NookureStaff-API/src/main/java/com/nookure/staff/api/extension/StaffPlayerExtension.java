package com.nookure.staff.api.extension;

import com.google.inject.Inject;
import com.nookure.staff.api.StaffPlayerWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class StaffPlayerExtension {
    @Inject
    public StaffPlayerExtension(@NotNull final StaffPlayerWrapper player) {}

    /**
     * Called when a player is created
     */
    public void onPlayerCreate() {}

    /**
     * Called when a player is destroyed
     */
    public void onPlayerDestroy() {}

    public void onStaffModeEnabled() {}

    public void onStaffModeDisabled() {}
}
