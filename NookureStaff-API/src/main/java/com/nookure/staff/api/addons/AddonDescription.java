package com.nookure.staff.api.addons;

import com.nookure.staff.api.addons.annotations.Addon;
import java.util.Optional;

public class AddonDescription {
    private final String id;
    private final String version;
    private final String description;
    private final Object main;
    private final Addon.AddonPlatform platform;
    private final Addon addon;

    public AddonDescription(Addon addon, Object main) {
        this.addon = addon;
        this.id = addon.name();
        this.version = addon.version();
        this.description = addon.description();
        this.main = main;
        this.platform = addon.platform();
    }

    /**
     * Gets the addon id
     *
     * @return The addon id
     */
    public String getID() {
        return id;
    }

    /**
     * Gets the addon version
     *
     * @return addon version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the addon description
     *
     * @return addon description {@link Optional}
     */
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Gets the addon main class
     *
     * @return addon main class
     */
    public Object getMain() {
        return main;
    }

    /**
     * Gets the addon platform
     *
     * @return addon platform
     */
    public Addon.AddonPlatform getPlatform() {
        return platform;
    }

    /**
     * Gets the addon annotation
     *
     * @return addon annotation
     */
    public Addon getAddon() {
        return addon;
    }
}
