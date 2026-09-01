package com.nookure.staff.api.hook;

import com.nookure.staff.api.PlayerWrapper;
import org.jetbrains.annotations.NotNull;

public interface PermissionHook {
    @NotNull String getHighestGroup(@NotNull PlayerWrapper player);
}
