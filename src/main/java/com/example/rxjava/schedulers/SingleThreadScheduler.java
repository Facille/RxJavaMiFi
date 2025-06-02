package com.example.rxjava.schedulers;

import com.example.rxjava.core.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SingleThreadScheduler implements Scheduler {
    private final ExecutorService executor;

    public SingleThreadScheduler() {
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("rx-single-thread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}