package com.nookure.staff.api.messaging;

import com.google.inject.Injector;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.PlayerWrapper;
import com.nookure.staff.api.event.Event;
import com.nookure.staff.api.event.server.BroadcastMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventMessengerTest {

    private EventMessenger messenger;

    @BeforeEach
    public void setUp() {
        Logger dummyLogger = new Logger() {
            @Override public void info(Component component) {}
            @Override public void warning(Component component) {}
            @Override public void severe(Component component) {}
            @Override public void debug(Component component) {}
        };

        NookureStaff dummyPlugin = new NookureStaff() {
            @Override public Logger getPLogger() { return dummyLogger; }
            @Override public boolean isDebug() { return false; }
            @Override public void setDebug(boolean debug) {}
            @Override public void reload() {}
            @Override public File getPluginDataFolder() { return null; }
            @Override public InputStream getPluginResource(String s) { return null; }
            @Override public Injector getInjector() { return null; }
            @Override public String getPrefix() { return ""; }
        };

        messenger = new EventMessenger(dummyLogger, dummyPlugin) {
            @Override public void prepare() {}
            @Override public void publish(@NotNull PlayerWrapper sender, byte @NotNull [] data) {}
        };
    }

    private byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            oos.flush();
        }
        return baos.toByteArray();
    }

    @Test
    public void testDecodeValidEvent() throws Exception {
        BroadcastMessage original = new BroadcastMessage("Staff alert test", "staff.see", true);
        byte[] data = serialize(original);

        Optional<Event> decoded = messenger.decodeEvent(data);
        Assertions.assertTrue(decoded.isPresent(), "Valid Event should be successfully decoded");
        Assertions.assertTrue(decoded.get() instanceof BroadcastMessage, "Decoded object should be BroadcastMessage");
        BroadcastMessage result = (BroadcastMessage) decoded.get();
        Assertions.assertEquals("Staff alert test", result.message());
        Assertions.assertEquals("staff.see", result.permission());
        Assertions.assertTrue(result.showInConsole());
    }

    @Test
    public void testDecodeNonEventObject() throws Exception {
        byte[] data = serialize("Just a raw string, not an Event");

        Optional<Event> decoded = messenger.decodeEvent(data);
        Assertions.assertTrue(decoded.isEmpty(), "Non-Event object must be rejected");
    }

    @Test
    public void testFilterRejectsDisallowedClass() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "val");
        byte[] data = serialize(map);

        Optional<Event> decoded = messenger.decodeEvent(data);
        Assertions.assertTrue(decoded.isEmpty(), "Disallowed classes must be blocked by ObjectInputFilter");
    }

    @Test
    public void testDecodeMalformedPayload() {
        byte[] malformed = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};

        Optional<Event> decoded = messenger.decodeEvent(malformed);
        Assertions.assertTrue(decoded.isEmpty(), "Malformed bytes should safely return Optional.empty()");
    }

    @Test
    public void testFilterRules() {
        ObjectInputFilter filter = EventMessenger.EVENT_INPUT_FILTER;

        // Allowed
        Assertions.assertEquals(ObjectInputFilter.Status.ALLOWED,
                filter.checkInput(filterInfoForClass(BroadcastMessage.class)));
        Assertions.assertEquals(ObjectInputFilter.Status.ALLOWED,
                filter.checkInput(filterInfoForClass(String.class)));
        Assertions.assertEquals(ObjectInputFilter.Status.ALLOWED,
                filter.checkInput(filterInfoForClass(java.util.UUID.class)));

        // Disallowed / Dangerous
        Assertions.assertEquals(ObjectInputFilter.Status.REJECTED,
                filter.checkInput(filterInfoForClass(ProcessBuilder.class)));
        Assertions.assertEquals(ObjectInputFilter.Status.REJECTED,
                filter.checkInput(filterInfoForClass(HashMap.class)));
    }

    private ObjectInputFilter.FilterInfo filterInfoForClass(Class<?> clazz) {
        return new ObjectInputFilter.FilterInfo() {
            @Override public Class<?> serialClass() { return clazz; }
            @Override public long arrayLength() { return 0; }
            @Override public long depth() { return 1; }
            @Override public long references() { return 1; }
            @Override public long streamBytes() { return 100; }
        };
    }
}
