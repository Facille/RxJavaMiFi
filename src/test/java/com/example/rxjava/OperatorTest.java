package com.example.rxjava;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.functions.ObservableOnSubscribe;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class OperatorTest {

    @Test
    void testFlatMapOperator() {
        List<Integer> results = new CopyOnWriteArrayList<>();
        AtomicInteger completed = new AtomicInteger(0);

        Observable.just(1, 2, 3)
                .flatMap(x -> Observable.just(x * 10, x * 20))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        results.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {
                        completed.incrementAndGet();
                    }
                });

        // Даем время на выполнение
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(1, completed.get());
        assertEquals(6, results.size());
        assertTrue(results.containsAll(Arrays.asList(10, 20, 20, 40, 30, 60)));
    }

    @Test
    void testErrorHandling() {
        AtomicBoolean errorReceived = new AtomicBoolean(false);

        // Явное создание ObservableOnSubscribe через анонимный класс
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(Observer<? super String> emitter) throws Exception {
                        emitter.onNext("Hello");
                        throw new RuntimeException("Test error");
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {}

                    @Override
                    public void onError(Throwable t) {
                        errorReceived.set(true);
                    }

                    @Override
                    public void onComplete() {
                        fail("Should not complete");
                    }
                });

        assertTrue(errorReceived.get());
    }
}