package com.nookure.staff.api.messaging;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.event.Event;
import java.io.*;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class EventMessenger implements AutoCloseable {
    private final Logger logger;
    private final NookureStaff plugin;

    @Inject
    public EventMessenger(@NotNull final Logger logger, @NotNull final NookureStaff plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    /**
     * Prepares the event transport for use.
     */
    public abstract void prepare();

    /**
     * Publishes an event to the event bus.
     *
     * @param sender The sender of the event
     * @param data   The event data
     */
    public abstract void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data);

    public void publish(@NotNull PlayerWrapper sender, @NotNull Event event) {
        Objects.requireNonNull(sender);
        Objects.requireNonNull(event);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(event);
            objectOutputStream.flush();

            publish(sender, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            logger.severe("Error while serializing event");
            if (plugin.isDebug()) {
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull public Optional<Event> decodeEvent(byte @NotNull [] message) {
        Objects.requireNonNull(message);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(message));
            return Optional.of((Event) objectInputStream.readObject());
        } catch (Exception e) {
            logger.severe("Error while decoding event from object stream");
            if (plugin.isDebug()) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    @Override
    public void close() throws Exception {}
}
