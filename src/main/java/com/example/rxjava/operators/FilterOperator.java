package com.example.rxjava.operators;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.functions.Predicate; // Наш кастомный Predicate

public class FilterOperator<T> extends Observable<T> {
    private final Observable<T> source;
    private final Predicate<? super T> predicate;

    public FilterOperator(Observable<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new FilterObserver<>(observer, predicate));
    }

    static class FilterObserver<T> implements Observer<T> {
        private final Observer<? super T> downstream;
        private final Predicate<? super T> predicate;

        FilterObserver(Observer<? super T> downstream, Predicate<? super T> predicate) {
            this.downstream = downstream;
            this.predicate = predicate;
        }

        @Override
        public void onNext(T item) {
            try {
                if (predicate.test(item)) {
                    downstream.onNext(item);
                }
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }
    }
}