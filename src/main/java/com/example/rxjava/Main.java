package com.example.rxjava;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.functions.ObservableOnSubscribe;
import com.example.rxjava.schedulers.ComputationScheduler;
import com.example.rxjava.schedulers.SingleThreadScheduler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting RxJava demo...");

        Observable.<Integer>create((ObservableOnSubscribe<Integer>) emitter -> {
                    System.out.println("Emitting on thread: " + Thread.currentThread().getName());
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onComplete();
                })
                
                .map(x -> x * 10)

                .filter(x -> x > 15)
                .subscribeOn(new ComputationScheduler())
                .observeOn(new SingleThreadScheduler())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        System.out.println("Received: " + item + " on thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Error: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Completed on thread: " + Thread.currentThread().getName());
                    }
                });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}