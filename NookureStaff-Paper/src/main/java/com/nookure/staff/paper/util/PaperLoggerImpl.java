package com.nookure.staff.paper.util;

import com.nookure.staff.api.Logger;
import com.nookure.staff.paper.bootstrap.StaffBootstrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

public class PaperLoggerImpl implements Logger {
    private final StaffBootstrapper boot;

    public PaperLoggerImpl(StaffBootstrapper boot) {
        this.boot = boot;
    }

    @Override
    public void info(Component component) {
        Bukkit.getConsoleSender().sendMessage(getDefaultStyle(component, NamedTextColor.GRAY, "INFO"));
    }

    @Override
    public void warning(Component component) {
        Bukkit.getConsoleSender().sendMessage(getDefaultStyle(component, NamedTextColor.YELLOW, "WARN"));
    }

    @Override
    public void severe(Component component) {
        Bukkit.getConsoleSender().sendMessage(getDefaultStyle(component, NamedTextColor.RED, "SEVERE"));
    }

    @Override
    public void debug(Component component) {
        if (boot.isDebug())
            Bukkit.getConsoleSender().sendMessage(getDefaultStyle(component, NamedTextColor.GRAY, "DEBUG"));
    }

    protected Component getDefaultStyle(Component component, NamedTextColor color, String mode) {
        return Component.text("NookureStaff")
                .color(NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(" | ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(mode).color(color))
                .append(Component.text(" > ").color(NamedTextColor.LIGHT_PURPLE))
                .append(component);
    }
}
