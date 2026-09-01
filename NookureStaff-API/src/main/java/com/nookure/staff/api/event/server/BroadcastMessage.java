package com.nookure.staff.api.event.server;

import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.event.Event;
import org.jetbrains.annotations.NotNull;

public record BroadcastMessage(
        @NotNull String message, @NotNull String permission, boolean showInConsole) implements Event {
    public BroadcastMessage(@NotNull String message) {
        this(message, Permissions.STAFF_PERMISSION, true);
    }

    public BroadcastMessage(@NotNull String message, @NotNull String permission) {
        this(message, permission, true);
    }
}
