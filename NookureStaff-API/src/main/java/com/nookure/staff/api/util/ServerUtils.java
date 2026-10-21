package com.nookure.staff.api.util;

import com.nookure.staff.api.Permissions;
import com.nookure.staff.api.PlayerWrapper;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public abstract class ServerUtils {
    public static final boolean isBukkit;
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
     * @param message       The message to broadcast
     * @param permission    The permission to check
     * @param showInConsole Whether to show the message in the console
     */
    public void broadcast(@NotNull String message, @NotNull String permission, boolean showInConsole) {
        broadcast(message, permission, showInConsole, true);
    }

    /**
     * Broadcast a message to the players with a specific permission
     *
     * @param message       The message to broadcast
     * @param permission    The permission to check
     * @param showInConsole Whether to show the message in the console
     * @param papi          Whether to use PlaceholderAPI
     */
    public abstract void broadcast(
            @NotNull String message, @NotNull String permission, boolean showInConsole, boolean papi);

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
        isBukkit = hasClass("org.bukkit.Bukkit");
        isPaper = isBukkit
                && (hasClass("com.destroystokyo.paper.PaperConfig")
                        || hasClass("io.papermc.paper.configuration.Configuration"));

        MINECRAFT_VERSION = getMinecraftVersionOrDefault();
    }

    private static MinecraftVersion getMinecraftVersionOrDefault() {
        if (!isBukkit) {
            return new MinecraftVersion(0, 0, 0);
        }
        try {
            try {
                return BukkitVersionHelper.getMinecraftVersionByPaper();
            } catch (Throwable e) {
                return BukkitVersionHelper.getMinecraftVersionByBukkit();
            }
        } catch (Throwable e) {
            logger.warning("Failed to get the Minecraft version, defaulting to 0.0.0");
            logger.warning("Keep in mind that this may cause issues with the plugin");
            logger.warning("Because of this, it is recommended to use the latest version of the software");
            return new MinecraftVersion(0, 0, 0);
        }
    }

    public static boolean isMock() {
        if (!isBukkit) {
            return false;
        }
        try {
            return BukkitVersionHelper.isMock();
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static Executor defaultAsyncExecutor() {
        return command -> {
            if (isMock()) {
                command.run();
            } else {
                ForkJoinPool.commonPool().execute(command);
            }
        };
    }

    public static ExecutorService asyncExecutor(@NotNull String threadPrefix) {
        ExecutorService virtualExecutor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name(threadPrefix, 0).factory());
        return new AbstractExecutorService() {
            private volatile boolean shutdown = false;

            @Override
            public void shutdown() {
                shutdown = true;
                virtualExecutor.shutdown();
            }

            @Override
            public @NotNull List<Runnable> shutdownNow() {
                shutdown = true;
                return virtualExecutor.shutdownNow();
            }

            @Override
            public boolean isShutdown() {
                return shutdown;
            }

            @Override
            public boolean isTerminated() {
                return shutdown && virtualExecutor.isTerminated();
            }

            @Override
            public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
                return virtualExecutor.awaitTermination(timeout, unit);
            }

            @Override
            public void execute(@NotNull Runnable command) {
                if (isMock()) {
                    command.run();
                } else {
                    virtualExecutor.execute(command);
                }
            }
        };
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
        } catch (Throwable e) {
            return false;
        }
    }
}
