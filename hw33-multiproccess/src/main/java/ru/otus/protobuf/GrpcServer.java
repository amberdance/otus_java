package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.server.NumberGeneratorService;
import ru.otus.protobuf.server.NumberGeneratorGrpcImpl;

import java.io.IOException;

@Slf4j
public class GrpcServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var service = new NumberGeneratorService();
        var grpcService = new NumberGeneratorGrpcImpl(service);
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(grpcService)
                .build();

        server.start();
        log.info("Server is waiting for connections...");
        server.awaitTermination();
    }
}
