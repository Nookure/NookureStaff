package com.nookure.staff.api.addons;

import com.nookure.staff.api.addons.annotations.Addon;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class AddonsUtils {
    private static Reflections reflections;

    /**
     * Gets all the addons
     *
     * @return A set of all the addons
     */
    public static Set<Class<?>> getAddons() {
        return getReflections().getTypesAnnotatedWith(Addon.class);
    }

    /**
     * Gets all the addons that are going to be loaded on scan
     *
     * @return A set of all the addons for the platform
     */
    public static Set<Class<?>> getLoadOnScanAddons() {
        return getAddons().stream()
                .filter(c -> c.getAnnotation(Addon.class).loadOnScan())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all the addons for a specific platform
     *
     * @param platform The platform {@link Addon.AddonPlatform}
     * @return A set of all the addons for the platform
     */
    public static Set<Class<?>> getAddons(Addon.AddonPlatform platform) {
        return getLoadOnScanAddons().stream()
                .filter(c -> {
                    Addon addon = c.getAnnotation(Addon.class);
                    return addon.platform() == platform;
                })
                .collect(Collectors.toSet());
    }

    private static Reflections getReflections() {
        if (reflections == null) {
            reflections = new Reflections();
        }

        return reflections;
    }
}
