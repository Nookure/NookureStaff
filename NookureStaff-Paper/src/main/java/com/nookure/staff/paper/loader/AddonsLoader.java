package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.addons.AddonManager;
import com.nookure.staff.api.util.AbstractLoader;
import java.io.File;
import java.io.IOException;

public class AddonsLoader implements AbstractLoader {
    @Inject
    private NookureStaff instance;

    @Inject
    private AddonManager manager;

    @Inject
    private Logger logger;

    @Override
    public void load() {
        File scanFolder = new File(instance.getPluginDataFolder(), "addons");

        if (!scanFolder.exists()) {
            if (scanFolder.mkdirs()) {
                logger.debug("Created addons folder!");
            }
        }

        try {
            manager.loadAddonsToClasspath(scanFolder.toPath());
        } catch (IOException e) {
            logger.severe("Could not load to the classpath the addons in the addons folder");
            logger.severe("Please contact us for support");
            logger.severe(e);
        } catch (Exception e) {
            logger.severe("An error ocurred while loading the addons");
            logger.severe(e);
        }
    }
}
