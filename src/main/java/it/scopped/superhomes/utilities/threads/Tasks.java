package it.scopped.superhomes.utilities.threads;

import it.scopped.superhomes.SuperHomes;

import java.util.Objects;

public class Tasks {
    public static void run(Callable callable, boolean async) {
        Objects.requireNonNull(callable);
        if (async)
            SuperHomes.getInstance().getServer().getScheduler().runTaskAsynchronously(SuperHomes.getInstance(), callable::call);
        else
            SuperHomes.getInstance().getServer().getScheduler().runTask(SuperHomes.getInstance(), callable::call);
    }

    public static void runLater(Callable callable, long delay, boolean async) {
        Objects.requireNonNull(callable);
        if (async)
            SuperHomes.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(SuperHomes.getInstance(), callable::call, delay);
        else
            SuperHomes.getInstance().getServer().getScheduler().runTaskLater(SuperHomes.getInstance(), callable::call, delay);
    }

    public static void runTimer(Callable callable, long delay, long interval, boolean async) {
        Objects.requireNonNull(callable);
        if (async)
            SuperHomes.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(SuperHomes.getInstance(), callable::call, delay, interval);
        else
            SuperHomes.getInstance().getServer().getScheduler().runTaskTimer(SuperHomes.getInstance(), callable::call, delay, interval);
    }

    public interface Callable {
        void call();
    }
}