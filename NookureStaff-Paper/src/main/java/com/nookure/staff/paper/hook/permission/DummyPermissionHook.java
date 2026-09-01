package com.nookure.staff.paper.hook.permission;

import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.hook.PermissionHook;
import org.jetbrains.annotations.NotNull;

public final class DummyPermissionHook implements PermissionHook {
    @Override
    public @NotNull String getHighestGroup(@NotNull PlayerWrapper player) {
        return "default";
    }
}
