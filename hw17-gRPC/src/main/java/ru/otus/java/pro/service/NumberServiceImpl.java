package ru.otus.java.pro.service;

import io.grpc.stub.StreamObserver;
import ru.otus.java.pro.NumberMessage;
import ru.otus.java.pro.NumbersRange;
import ru.otus.java.pro.NumbersServiceGrpc;

public class NumberServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    @Override
    public void generate(NumbersRange request, StreamObserver<NumberMessage> responseObserver) {

        int first = request.getFirstValue();
        int last = request.getLastValue();

        for (int i = first + 1; i <= last; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println("Interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }

            NumberMessage msg = NumberMessage.newBuilder().setValue(i).build();
            responseObserver.onNext(msg);
        }

        responseObserver.onCompleted();
    }
}
