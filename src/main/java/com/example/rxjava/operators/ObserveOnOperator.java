package com.example.rxjava.operators;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.core.Scheduler;

public class ObserveOnOperator<T> extends Observable<T> {
    private final Observable<T> source;
    private final Scheduler scheduler;

    public ObserveOnOperator(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new ObserveOnObserver<>(observer, scheduler));
    }

    static class ObserveOnObserver<T> implements Observer<T> {
        private final Observer<? super T> downstream;
        private final Scheduler scheduler;

        ObserveOnObserver(Observer<? super T> downstream, Scheduler scheduler) {
            this.downstream = downstream;
            this.scheduler = scheduler;
        }

        @Override
        public void onNext(T item) {
            scheduler.execute(() -> downstream.onNext(item));
        }

        @Override
        public void onError(Throwable t) {
            scheduler.execute(() -> downstream.onError(t));
        }

        @Override
        public void onComplete() {
            scheduler.execute(downstream::onComplete);
        }
    }
}