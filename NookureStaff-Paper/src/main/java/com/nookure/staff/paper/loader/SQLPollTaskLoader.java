package com.nookure.staff.paper.loader;

import com.google.inject.Inject;
import com.nookure.staff.messaging.sql.SQLPollTask;
import java.io.Closeable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SQLPollTaskLoader implements Closeable {
    private final BukkitTask pollTask;
    private final BukkitTask cleanupTask;

    @Inject
    public SQLPollTaskLoader(@NotNull final SQLPollTask sqlPollTask, @NotNull final JavaPlugin bootstrapper) {
        pollTask = Bukkit.getScheduler().runTaskTimerAsynchronously(bootstrapper, sqlPollTask::pollMessages, 0, 20);
        cleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(bootstrapper, sqlPollTask::cleanup, 0, 60 * 20);
    }

    @Override
    public void close() {
        pollTask.cancel();
        cleanupTask.cancel();
    }
}
