package com.example.rxjava.functions;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t) throws Exception;
}