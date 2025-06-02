package com.example.rxjava.operators;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.core.Scheduler;

public class SubscribeOnOperator<T> extends Observable<T> {
    private final Observable<T> source;
    private final Scheduler scheduler;

    public SubscribeOnOperator(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        scheduler.execute(() -> source.subscribe(observer));
    }
}