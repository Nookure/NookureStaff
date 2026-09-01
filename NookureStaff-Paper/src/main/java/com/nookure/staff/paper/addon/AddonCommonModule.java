package com.nookure.staff.paper.addon;

import com.google.inject.AbstractModule;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.addons.AddonContainer;
import com.nookure.staff.api.addons.AddonLogger;
import com.nookure.staff.api.addons.annotations.Addon;
import com.nookure.staff.api.addons.annotations.AddonDataFolder;
import java.io.File;

public class AddonCommonModule extends AbstractModule {
    private final AddonContainer addonContainer;
    private final Addon.AddonPlatform addonPlatform;
    private final File addonDataFolder;
    private final NookureStaff instance;
    private final Class<?> addonClass;

    public AddonCommonModule(AddonContainer addonContainer, NookureStaff instance, Class<?> addonClass) {
        this.addonContainer = addonContainer;
        this.addonPlatform = Addon.AddonPlatform.BUKKIT;
        this.instance = instance;
        addonDataFolder = new File(
                instance.getPluginDataFolder(),
                "/addons/" + addonContainer.getDescription().getID());

        if (!addonDataFolder.exists()) {
            addonDataFolder.mkdirs();
        }

        this.addonClass = addonClass;
    }

    @Override
    protected void configure() {
        bind(AddonContainer.class).toInstance(addonContainer);
        bind(Addon.AddonPlatform.class).toInstance(addonPlatform);
        bind(File.class).annotatedWith(AddonDataFolder.class).toInstance(addonDataFolder);
        bind(Logger.class)
                .annotatedWith(com.nookure.staff.api.addons.annotations.AddonLogger.class)
                .toInstance(new AddonLogger(addonContainer.getDescription().getID(), instance.getPLogger()));
        bind(AddonLogger.class)
                .toInstance(new AddonLogger(addonContainer.getDescription().getID(), instance.getPLogger()));
    }
}
