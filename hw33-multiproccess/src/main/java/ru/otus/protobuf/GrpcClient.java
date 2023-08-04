package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.NumberGeneratorGrpc;
import ru.otus.protobuf.client.NumberProcessor;

public class GrpcClient {

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 8190;
    private static final int SERVER_NUMBER_START = 0;
    private static final int SERVER_NUMBER_END = 30;
    private static final int CLIENT_NUMBER_START = 0;
    private static final int CLIENT_NUMBER_END = 50;


    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        var stub = NumberGeneratorGrpc.newStub(channel);
        var numberProcessor = new NumberProcessor(stub, CLIENT_NUMBER_START, CLIENT_NUMBER_END);

        numberProcessor.start(SERVER_NUMBER_START, SERVER_NUMBER_END);
        channel.shutdown();
    }
}
