package com.nookure.staff.api.util;

import org.bukkit.Bukkit;

final class BukkitVersionHelper {
    private BukkitVersionHelper() {}

    static ServerUtils.MinecraftVersion getMinecraftVersionByPaper() {
        String raw = Bukkit.getMinecraftVersion().split("-")[0];
        String[] version = raw.split("\\.");
        int major = Integer.parseInt(version[0]);
        int minor = version.length > 1 ? Integer.parseInt(version[1]) : 0;
        int patch = version.length > 2 ? Integer.parseInt(version[2]) : 0;
        return new ServerUtils.MinecraftVersion(major, minor, patch);
    }

    static ServerUtils.MinecraftVersion getMinecraftVersionByBukkit() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionSplit = version.split("_");
        return new ServerUtils.MinecraftVersion(
                Integer.parseInt(versionSplit[0]),
                Integer.parseInt(versionSplit[1]),
                versionSplit.length > 2 ? Integer.parseInt(versionSplit[2]) : 0);
    }

    static boolean isMock() {
        try {
            return Bukkit.getServer() != null
                    && (Bukkit.getName().equalsIgnoreCase("servermock")
                            || Bukkit.getServer().getClass().getName().contains("mockbukkit"));
        } catch (Throwable ignored) {
            return false;
        }
    }
}
