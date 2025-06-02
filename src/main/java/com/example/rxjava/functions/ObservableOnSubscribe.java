package com.example.rxjava.functions;

import com.example.rxjava.core.Observer;

@FunctionalInterface
public interface ObservableOnSubscribe<T> {
    void subscribe(Observer<? super T> emitter) throws Exception;
}