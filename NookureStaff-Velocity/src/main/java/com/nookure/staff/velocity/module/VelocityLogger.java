package com.nookure.staff.velocity.module;

import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaffPlatform;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VelocityLogger implements Logger {
    private final ProxyServer server;
    private final NookureStaffPlatform<ProxyServer> plugin;

    public VelocityLogger(ProxyServer server, NookureStaffPlatform<ProxyServer> plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public void info(Component component) {
        server.getConsoleCommandSource().sendMessage(getDefaultStyle(component, NamedTextColor.GRAY, "INFO"));
    }

    @Override
    public void warning(Component component) {
        server.getConsoleCommandSource().sendMessage(getDefaultStyle(component, NamedTextColor.YELLOW, "WARN"));
    }

    @Override
    public void severe(Component component) {
        server.getConsoleCommandSource().sendMessage(getDefaultStyle(component, NamedTextColor.RED, "SEVERE"));
    }

    @Override
    public void debug(Component component) {
        if (plugin.isDebug())
            server.getConsoleCommandSource().sendMessage(getDefaultStyle(component, NamedTextColor.GRAY, "DEBUG"));
    }

    private Component getDefaultStyle(Component component, NamedTextColor color, String mode) {
        return Component.text("NookureStaff")
                .color(NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(" | ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(mode).color(color))
                .append(Component.text(" > ").color(NamedTextColor.LIGHT_PURPLE))
                .append(component);
    }
}
