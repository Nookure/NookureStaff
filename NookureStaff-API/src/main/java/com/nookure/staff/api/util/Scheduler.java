package com.nookure.staff.api.util;

/**
 * A scheduler that can be used to schedule tasks.
 * <p>
 * This class is a placeholder for the actual scheduler.
 * The actual scheduler will be implemented in the platform-specific module.
 * This class is used to avoid platform-specific code in the core module.
 * The actual scheduler will be injected using the Guice framework.
 * <p>
 */
public abstract class Scheduler {
    /**
     * Schedule a task to run asynchronously.
     *
     * @param runnable the task to run
     */
    public int async(Runnable runnable) {
        return async(runnable, 0);
    }

    /**
     * Schedule a task to run synchronously.
     *
     * @param runnable the task to run
     */
    public int sync(Runnable runnable) {
        return sync(runnable, 0);
    }

    /**
     * Schedule a task to run asynchronously.
     *
     * @param runnable the task to run
     * @param delay    the delay before the task starts
     */
    public abstract int async(Runnable runnable, long delay);

    /**
     * Schedule a task to run synchronously.
     *
     * @param runnable the task to run
     * @param delay    the delay before the task starts
     */
    public abstract int sync(Runnable runnable, long delay);

    /**
     * Schedule a task to run asynchronously.
     *
     * @param runnable the task to run
     * @param delay    the delay before the task starts
     * @param period   the period between each execution
     */
    public abstract int async(Runnable runnable, long delay, long period);

    /**
     * Schedule a task to run synchronously.
     *
     * @param runnable the task to run
     * @param delay    the delay before the task starts
     * @param period   the period between each execution
     */
    public abstract int sync(Runnable runnable, long delay, long period);

    /**
     * Cancel a task.
     *
     * @param taskId the id of the task to cancel
     */
    public abstract void cancel(int taskId);
}
