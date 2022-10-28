package com.android.demo.rxandroid.schedule;

import java.util.concurrent.TimeUnit;

import com.android.demo.rxandroid.disposable.Disposable;

public abstract class Scheduler {
    public abstract Worker createWorker();

    public abstract static class Worker implements Disposable {
        public abstract Disposable schedule(Runnable run, long delay, TimeUnit unit);
    }

    public Disposable schedule(final Runnable run, long delay, TimeUnit unit) {
        Worker worker = createWorker();
        return worker.schedule(run, delay, unit);
    }

    public Disposable scheduleDirect(final Runnable run) {
        Worker worker = createWorker();
        return worker.schedule(run, 0,  TimeUnit.NANOSECONDS);
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long delay, long period, TimeUnit unit) {
        throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
    }
}
