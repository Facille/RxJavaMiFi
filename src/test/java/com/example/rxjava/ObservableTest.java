package com.example.rxjava;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.functions.ObservableOnSubscribe;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {

    @Test
    void testCreateAndSubscribe() {
        List<Integer> results = new ArrayList<>();
        AtomicBoolean completed = new AtomicBoolean(false);

        // Явное создание ObservableOnSubscribe через анонимный класс
        Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(Observer<? super Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onComplete();
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        results.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error");
                    }

                    @Override
                    public void onComplete() {
                        completed.set(true);
                    }
                });

        assertTrue(completed.get());
        assertEquals(2, results.size());
        assertEquals(1, results.get(0));
        assertEquals(2, results.get(1));
    }

    @Test
    void testMapOperator() {
        List<String> results = new ArrayList<>();

        Observable.just(1, 2, 3)
                .map(x -> "Number: " + x)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String item) {
                        results.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error");
                    }

                    @Override
                    public void onComplete() {}
                });

        assertEquals(3, results.size());
        assertEquals("Number: 1", results.get(0));
        assertEquals("Number: 3", results.get(2));
    }

    @Test
    void testFilterOperator() {
        List<Integer> results = new ArrayList<>();

        Observable.just(10, 20, 30, 40)
                .filter(x -> x > 25)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        results.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {
                        fail("Unexpected error");
                    }

                    @Override
                    public void onComplete() {}
                });

        assertEquals(2, results.size());
        assertEquals(30, results.get(0));
        assertEquals(40, results.get(1));
    }
}