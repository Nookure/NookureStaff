package com.nookure.staff.api.extension;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.stream.Stream;

@Singleton
public class StaffPlayerExtensionManager {
    public record Extension(
            Class<? extends StaffPlayerExtension> extension, Class<? extends StaffPlayerExtension> base) {}

    private final ArrayList<Extension> extensions = new ArrayList<>();

    public void registerExtension(
            Class<? extends StaffPlayerExtension> extension, Class<? extends StaffPlayerExtension> base) {
        extensions.add(new Extension(extension, base));
    }

    public void registerExtension(Class<? extends StaffPlayerExtension> extension) {
        extensions.add(new Extension(extension, extension));
    }

    public Stream<Extension> getExtensionsStream() {
        return extensions.stream();
    }

    public void clearExtensions() {
        extensions.clear();
    }
}
