package com.example.rxjava;

import com.example.rxjava.core.Observable;
import com.example.rxjava.core.Observer;
import com.example.rxjava.schedulers.ComputationScheduler;
import com.example.rxjava.schedulers.SingleThreadScheduler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    @Test
    void testSubscribeOn() throws InterruptedException {
        AtomicReference<String> emitThread = new AtomicReference<>();
        AtomicReference<Boolean> received = new AtomicReference<>(false);
        CountDownLatch latch = new CountDownLatch(1);

        // Явное указание типа Observable<Integer>
        Observable.<Integer>create(emitter -> {
                    emitThread.set(Thread.currentThread().getName());
                    emitter.onNext(42);
                    emitter.onComplete();
                    latch.countDown();
                })
                .subscribeOn(new ComputationScheduler())
                .subscribe(new Observer<Integer>() {  // Используем Observer<Integer>
                    @Override
                    public void onNext(Integer item) {
                        received.set(true);
                    }

                    @Override
                    public void onError(Throwable t) {
                        latch.countDown();
                    }

                    @Override
                    public void onComplete() {
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "Timeout waiting for observable");
        assertNotNull(emitThread.get(), "Emit thread should not be null");
        assertTrue(emitThread.get().contains("computation") ||
                        emitThread.get().contains("pool"),
                "Thread name should contain 'computation' or 'pool'. Actual: " + emitThread.get());
        assertTrue(received.get(), "Value should be received");
    }

    @Test
    void testObserveOn() throws InterruptedException {
        AtomicReference<String> receiveThread = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Observable.just(1)
                .observeOn(new SingleThreadScheduler())
                .subscribe(new Observer<Integer>() {  // Используем Observer<Integer>
                    @Override
                    public void onNext(Integer item) {
                        receiveThread.set(Thread.currentThread().getName());
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable t) {
                        latch.countDown();
                    }

                    @Override
                    public void onComplete() {
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "Timeout waiting for observable");
        assertNotNull(receiveThread.get(), "Receive thread should not be null");
        assertTrue(receiveThread.get().contains("single") ||
                        receiveThread.get().contains("pool"),
                "Thread name should contain 'single' or 'pool'. Actual: " + receiveThread.get());
    }
}