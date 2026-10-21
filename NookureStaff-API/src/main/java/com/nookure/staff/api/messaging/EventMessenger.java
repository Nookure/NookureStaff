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

    public static final ObjectInputFilter EVENT_INPUT_FILTER = filterInfo -> {
        if (filterInfo.depth() > 10) {
            return ObjectInputFilter.Status.REJECTED;
        }
        if (filterInfo.references() > 500) {
            return ObjectInputFilter.Status.REJECTED;
        }
        if (filterInfo.streamBytes() > 1048576) {
            return ObjectInputFilter.Status.REJECTED;
        }
        if (filterInfo.arrayLength() > 1000) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> clazz = filterInfo.serialClass();
        if (clazz == null) {
            return ObjectInputFilter.Status.UNDECIDED;
        }
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        if (clazz.isPrimitive()) {
            return ObjectInputFilter.Status.ALLOWED;
        }
        String name = clazz.getName();
        if (name.startsWith("com.nookure.staff.api.")
                || name.startsWith("java.lang.")
                || name.equals("java.util.UUID")
                || name.equals("java.util.Date")
                || name.startsWith("java.time.")) {
            if (name.startsWith("java.lang.reflect.")
                    || name.equals("java.lang.ProcessBuilder")
                    || name.equals("java.lang.Runtime")) {
                return ObjectInputFilter.Status.REJECTED;
            }
            return ObjectInputFilter.Status.ALLOWED;
        }
        return ObjectInputFilter.Status.REJECTED;
    };

    @NotNull public Optional<Event> decodeEvent(byte @NotNull [] message) {
        Objects.requireNonNull(message);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            objectInputStream.setObjectInputFilter(EVENT_INPUT_FILTER);
            Object object = objectInputStream.readObject();
            if (object instanceof Event event) {
                return Optional.of(event);
            }
            logger.warning("Decoded object is not an instance of Event: %s", object != null ? object.getClass().getName() : "null");
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
