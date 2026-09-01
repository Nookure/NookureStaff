package com.nookure.staff.paper.util;

import com.nookure.staff.api.util.Scheduler;

public class MockScheduler extends Scheduler {

    @Override
    public int async(Runnable runnable, long delay) {
        runnable.run();
        return 0;
    }

    @Override
    public int sync(Runnable runnable, long delay) {
        runnable.run();
        return 0;
    }

    @Override
    public int async(Runnable runnable, long delay, long period) {
        return 0;
    }

    @Override
    public int sync(Runnable runnable, long delay, long period) {
        return 0;
    }

    @Override
    public void cancel(int taskId) {}
}
