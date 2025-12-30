package ru.otus.java.pro;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.service.NumberServiceImpl;

public class NumbersGrpcServer {

    private static final Logger log = LoggerFactory.getLogger(NumbersGrpcServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException, IOException {

        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumberServiceImpl())
                .build();

        server.start();
        log.info("Numbers server started!");
        log.info("waiting for client ...");
        server.awaitTermination();
    }
}
