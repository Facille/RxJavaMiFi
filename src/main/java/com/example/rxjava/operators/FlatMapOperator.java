package com.example.rxjava.operators;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.functions.Function; // Наш кастомный Function

public class FlatMapOperator<T, R> extends Observable<R> {
    private final Observable<T> source;
    private final Function<? super T, ? extends Observable<? extends R>> mapper;

    public FlatMapOperator(Observable<T> source, Function<? super T, ? extends Observable<? extends R>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(Observer<? super R> observer) {
        source.subscribe(new FlatMapObserver<>(observer, mapper));
    }

    static class FlatMapObserver<T, R> implements Observer<T> {
        private final Observer<? super R> downstream;
        private final Function<? super T, ? extends Observable<? extends R>> mapper;

        FlatMapObserver(Observer<? super R> downstream, Function<? super T, ? extends Observable<? extends R>> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T item) {
            try {
                Observable<? extends R> observable = mapper.apply(item);
                observable.subscribe(new Observer<R>() {
                    @Override
                    public void onNext(R value) {
                        downstream.onNext(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        downstream.onError(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
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