package com.example.rxjava.core;

public interface Scheduler {
    void execute(Runnable task);
}