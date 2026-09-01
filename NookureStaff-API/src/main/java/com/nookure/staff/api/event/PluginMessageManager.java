package com.nookure.staff.api.event;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class PluginMessageManager<P> {
    @Inject
    private Logger logger;

    @Inject
    private NookureStaff plugin;

    public abstract void sendEvent(@NotNull Event event, @NotNull P player);

    @NotNull public Optional<Event> decodeEvent(@NotNull ObjectInputStream objetStream) {
        Objects.requireNonNull(objetStream);

        try {
            return Optional.of((Event) objetStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error while decoding event from object stream");
            if (plugin.isDebug()) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }
}
