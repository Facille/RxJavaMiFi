package com.example.rxjava.core;

import com.example.rxjava.functions.Function;
import com.example.rxjava.functions.Predicate;
import com.example.rxjava.functions.ObservableOnSubscribe;
import com.example.rxjava.operators.*;

import java.util.function.Consumer;

public abstract class Observable<T> {

    private static class ObservableCreate<T> extends Observable<T> {
        private final ObservableOnSubscribe<T> source;

        ObservableCreate(ObservableOnSubscribe<T> source) {
            this.source = source;
        }

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            try {
                source.subscribe(observer);
            } catch (Exception e) {
                observer.onError(e);
            }
        }
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableCreate<>(source);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);

    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return new MapOperator<>(this, mapper);
    }

    public final Observable<T> filter(Predicate<? super T> predicate) {
        return new FilterOperator<>(this, predicate);
    }

    public final <R> Observable<R> flatMap(Function<? super T, ? extends Observable<? extends R>> mapper) {
        return new FlatMapOperator<>(this, mapper);
    }

    public final Observable<T> subscribeOn(Scheduler scheduler) {
        return new SubscribeOnOperator<>(this, scheduler);
    }

    public final Observable<T> observeOn(Scheduler scheduler) {
        return new ObserveOnOperator<>(this, scheduler);
    }
    public static <T> Observable<T> just(T... items) {
        return create(emitter -> {
            for (T item : items) {
                emitter.onNext(item);
            }
            emitter.onComplete();
        });
    }

    public void subscribe(
            Consumer<? super T> onNext,
            Consumer<? super Throwable> onError,
            Runnable onComplete
    ) {
        subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    onNext.accept(item);
                } catch (Exception e) {
                    onError.accept(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                onError.accept(t);
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }
        });
    }
}