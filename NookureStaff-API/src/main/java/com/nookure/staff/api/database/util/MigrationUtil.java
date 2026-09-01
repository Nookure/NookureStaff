package com.nookure.staff.api.database.util;

import java.io.IOException;
import java.io.StringReader;
import org.jetbrains.annotations.NotNull;

public class MigrationUtil {
    @NotNull public static String fromClasspath(@NotNull final String path) {
        final var classLoader = MigrationUtil.class.getClassLoader();

        try (var resource = classLoader.getResourceAsStream(path)) {
            if (resource == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }

            StringReader reader = new StringReader(new String(resource.readAllBytes()));

            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }

            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
