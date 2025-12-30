package ru.otus.java.pro.client;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.NumberMessage;

public class ClientStreamObserver implements StreamObserver<NumberMessage> {

    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private final CountDownLatch latch;

    private final AtomicInteger lastValueFromServer = new AtomicInteger(0);

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    public int getAndResetLastValue() {
        return lastValueFromServer.getAndSet(0);
    }

    @Override
    public void onNext(NumberMessage numberMessage) {
        int number = numberMessage.getValue();
        log.info("new value:{}", number);
        lastValueFromServer.set(number);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Communication error", throwable);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("Request completed");
        latch.countDown();
    }
}
