package com.nookure.staff.api.extension.staff;

import com.nookure.staff.api.StaffPlayerWrapper;
import com.nookure.staff.api.extension.StaffPlayerExtension;
import com.nookure.staff.api.item.StaffItem;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class StaffModeExtension extends StaffPlayerExtension {
    public StaffModeExtension(StaffPlayerWrapper player) {
        super(player);
    }

    public abstract void enableStaffMode(final boolean silentJoin);

    public abstract void disableStaffMode();

    public abstract void checkStaffMode();

    public abstract void writeStaffModeState(final boolean state);

    public abstract void toggleStaffMode(boolean silentJoin);

    public abstract boolean isStaffMode();

    public abstract void setItems();

    @NotNull public abstract Map<Integer, StaffItem> getItems();

    public abstract void saveInventory();

    public abstract void clearInventory();

    public abstract void restoreInventory();

    public abstract void saveLocation();

    public abstract void restoreLocation();
}
