package ru.otus;

import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GrpcServer {
    public static final int SERVER_PORT = 8090;

    public static void main(String[] args) throws IOException, InterruptedException {
        io.grpc.Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new ExchangeServiceImpl())
                .build();

        server.start();
        log.info("Server awaiting connections...");
        server.awaitTermination();
    }
}
