package com.example.rxjava.schedulers;

import com.example.rxjava.core.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ComputationScheduler implements Scheduler {
    private final ExecutorService executor;

    public ComputationScheduler() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(coreCount, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("rx-computation-thread");
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