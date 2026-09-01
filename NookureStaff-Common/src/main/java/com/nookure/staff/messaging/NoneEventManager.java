package com.nookure.staff.messaging;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.event.Event;
import com.nookure.staff.api.event.EventManager;
import com.nookure.staff.api.messaging.EventMessenger;
import org.jetbrains.annotations.NotNull;

public class NoneEventManager extends EventMessenger {
    private final EventManager eventManager;

    @Inject
    public NoneEventManager(@NotNull Logger logger, @NotNull NookureStaff plugin, @NotNull EventManager eventManager) {
        super(logger, plugin);
        this.eventManager = eventManager;
    }

    @Override
    public void prepare() {
        // Nothing to do here
    }

    @Override
    public void publish(@NotNull PlayerWrapper sender, @NotNull Event event) {
        eventManager.fireEvent(event);
    }

    @Override
    public void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data) {}
}
