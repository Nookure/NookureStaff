package com.nookure.staff.api.util;

import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.PlayerWrapper;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public abstract class ServerUtils {
    public static final boolean isPaper;
    public static final MinecraftVersion MINECRAFT_VERSION;
    private static final Logger logger = Logger.getLogger("NookureStaff");

    /**
     * Check if a player is online
     *
     * @param uuid The UUID of the player
     * @return true if the player is online, false otherwise
     */
    public abstract boolean isOnline(@NotNull UUID uuid);

    /**
     * Check if a player is online
     *
     * @param name The name of the player
     * @return true if the player is online, false otherwise
     */
    public abstract boolean isOnline(@NotNull String name);

    /**
     * Broadcast a message to the players with a specific permission
     *
     * @param message    The message to broadcast
     * @param permission The permission to check
     */
    public abstract void broadcast(@NotNull String message, @NotNull String permission, boolean showInConsole);

    /**
     * Broadcast a message to the players with a specific permission
     *
     * @param message    The message to broadcast
     * @param permission The permission to check
     */
    public void broadcast(@NotNull String message, @NotNull String permission) {
        broadcast(message, permission, true);
    }

    /**
     * Broadcast a message to the players with the permission {@link Permissions#STAFF_PERMISSION}
     *
     * @param message The message to broadcast
     */
    public void broadcastStaffMessage(@NotNull String message) {
        broadcast(message, Permissions.STAFF_PERMISSION);
    }

    /**
     * Send a command to the proxy server console
     *
     * @param command The command to send
     * @param sender  The sender of the command
     */
    public abstract void sendCommandToProxy(@NotNull String command, @NotNull PlayerWrapper sender);

    static {
        isPaper = hasClass("com.destroystokyo.paper.PaperConfig")
                || hasClass("io.papermc.paper.configuration.Configuration");

        MINECRAFT_VERSION = getMinecraftVersionOrDefault();
    }

    private static MinecraftVersion getMinecraftVersionOrDefault() {
        try {
            try {
                return getMinecraftVersionByPaper();
            } catch (Exception e) {
                return getMinecraftVersionByBukkit();
            }
        } catch (Exception e) {
            logger.warning("Failed to get the Minecraft version, defaulting to 0.0.0");
            logger.warning("Keep in mind that this may cause issues with the plugin");
            logger.warning("Because of this, it is recommended to use the latest version of the software");
            return new MinecraftVersion(0, 0, 0);
        }
    }

    private static MinecraftVersion getMinecraftVersionByPaper() {
        String[] version = Bukkit.getMinecraftVersion().split("\\.");
        return new MinecraftVersion(
                Integer.parseInt(version[0]), Integer.parseInt(version[1]), Integer.parseInt(version[2]));
    }

    private static MinecraftVersion getMinecraftVersionByBukkit() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] versionSplit = version.split("_");
        return new MinecraftVersion(
                Integer.parseInt(versionSplit[0]),
                Integer.parseInt(versionSplit[1]),
                Integer.parseInt(versionSplit[2]));
    }

    public record MinecraftVersion(int major, int minor, int patch) {
        public boolean isAtLeast(int major, int minor, int patch) {
            return this.major > major
                    || (this.major == major && this.minor > minor)
                    || (this.major == major && this.minor == minor && this.patch >= patch);
        }

        public boolean isBefore(int major, int minor, int patch) {
            return this.major < major
                    || (this.major == major && this.minor < minor)
                    || (this.major == major && this.minor == minor && this.patch < patch);
        }

        public boolean isAtLeast(int major, int minor) {
            return isAtLeast(major, minor, 0);
        }

        public boolean isBefore(int major, int minor) {
            return isBefore(major, minor, 0);
        }
    }

    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
