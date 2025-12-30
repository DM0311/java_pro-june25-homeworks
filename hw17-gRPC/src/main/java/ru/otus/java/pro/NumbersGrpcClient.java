package ru.otus.java.pro;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.client.ClientStreamObserver;

public class NumbersGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersGrpcClient.class);

    private static final String SERVER_HOST = "localhost";

    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        log.info("numbers Client started...");

        CountDownLatch latch = new CountDownLatch(1);
        NumbersServiceGrpc.NumbersServiceStub asyncStub = NumbersServiceGrpc.newStub(channel);

        NumbersRange range =
                NumbersRange.newBuilder().setFirstValue(0).setLastValue(30).build();

        ClientStreamObserver clientObserver = new ClientStreamObserver(latch);
        asyncStub.generate(range, clientObserver);

        int currentValue = 0;

        for (int i = 0; i <= 50; i++) {
            int serverVal = clientObserver.getAndResetLastValue();
            currentValue = currentValue + serverVal + 1;
            log.info("currentValue:{}", currentValue);
            log.debug("Iteration cycle: {}", i);
            Thread.sleep(1000);
        }

        latch.await();
        channel.shutdown();
    }
}
