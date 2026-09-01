package com.nookure.staff.paper.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.util.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class PaperScheduler extends Scheduler {
    @Inject
    private JavaPlugin plugin;

    @Override
    public int async(Runnable runnable, long delay) {
        return plugin.getServer()
                .getScheduler()
                .runTaskLaterAsynchronously(plugin, runnable, delay)
                .getTaskId();
    }

    @Override
    public int sync(Runnable runnable, long delay) {
        return plugin.getServer()
                .getScheduler()
                .runTaskLater(plugin, runnable, delay)
                .getTaskId();
    }

    @Override
    public int async(Runnable runnable, long delay, long period) {
        return plugin.getServer()
                .getScheduler()
                .runTaskTimerAsynchronously(plugin, runnable, delay, period)
                .getTaskId();
    }

    @Override
    public int sync(Runnable runnable, long delay, long period) {
        return plugin.getServer()
                .getScheduler()
                .runTaskTimer(plugin, runnable, delay, period)
                .getTaskId();
    }

    @Override
    public void cancel(int taskId) {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }
}
