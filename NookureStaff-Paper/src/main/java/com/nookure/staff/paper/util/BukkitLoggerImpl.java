package com.nookure.staff.paper.util;

import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public final class BukkitLoggerImpl extends PaperLoggerImpl {
    private final StaffBootstrapper boot;

    public BukkitLoggerImpl(StaffBootstrapper boot) {
        super(boot);
        this.boot = boot;
    }

    @Override
    public void info(Component component) {
        Bukkit.getConsoleSender()
                .sendMessage(LegacyComponentSerializer.legacySection()
                        .serialize(getDefaultStyle(component, NamedTextColor.GRAY, "INFO")));
    }

    @Override
    public void warning(Component component) {
        Bukkit.getConsoleSender()
                .sendMessage(LegacyComponentSerializer.legacySection()
                        .serialize(getDefaultStyle(component, NamedTextColor.YELLOW, "WARN")));
    }

    @Override
    public void severe(Component component) {
        Bukkit.getConsoleSender()
                .sendMessage(LegacyComponentSerializer.legacySection()
                        .serialize(getDefaultStyle(component, NamedTextColor.RED, "SEVERE")));
    }

    @Override
    public void debug(Component component) {
        if (boot.isDebug())
            Bukkit.getConsoleSender()
                    .sendMessage(LegacyComponentSerializer.legacySection()
                            .serialize(getDefaultStyle(component, NamedTextColor.GRAY, "DEBUG")));
    }
}
