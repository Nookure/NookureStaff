package com.nookure.staff.api.addons;

import com.nookure.staff.api.Logger;
import net.kyori.adventure.text.Component;

public final class AddonLogger implements Logger {
    private final String prefix;
    private final Logger logger;

    public AddonLogger(String prefix, Logger logger) {
        this.prefix = prefix;
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        Logger.super.info("[" + prefix + "] " + message);
    }

    @Override
    public void warning(String message) {
        Logger.super.warning("[" + prefix + "] " + message);
    }

    @Override
    public void severe(String message) {
        Logger.super.severe("[" + prefix + "] " + message);
    }

    @Override
    public void debug(String message) {
        Logger.super.debug("[" + prefix + "] " + message);
    }

    @Override
    public void info(Component component) {
        logger.info(component);
    }

    @Override
    public void warning(Component component) {
        logger.warning(component);
    }

    @Override
    public void severe(Component component) {
        logger.severe(component);
    }

    @Override
    public void debug(Component component) {
        logger.debug(component);
    }
}
