package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class GrpcClient {

    private static final int maxValue = 50;
    private static final int firstValueInRequest = 1;
    private static final int lastValueInRequest = 30;
    private static final AtomicInteger valueFromServer = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        var channel = runStub();
        readAndIncrement();
        channel.shutdown();
    }

    private static void readAndIncrement() throws InterruptedException {
        for (int currentValue = 0; currentValue < maxValue; currentValue++) {
            int localVal = valueFromServer.getAndSet(0);

            log.info("currentValue before add increment = " + currentValue);
            log.info("valueFromServer = " + localVal);

            currentValue = currentValue + localVal + 1;

            if (valueFromServer.get() > 0) {
                valueFromServer.set(0);
            }

            log.info("currentValue after add increment = " + currentValue);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
    }

    private static ManagedChannel runStub() {
        var channel = ManagedChannelBuilder.forAddress("localhost", GrpcServer.SERVER_PORT)
                .usePlaintext()
                .build();

        var request = ExchangeMessageRequest.newBuilder()
                .setFirstValue(firstValueInRequest)
                .setLastValue(lastValueInRequest)
                .build();

        var stub = ExchangeServiceGrpc.newStub(channel);

        stub.send(request, new StreamObserver<>() {
            @Override
            public void onNext(ExchangeMessageResponse value) {
                log.info("Received value form server= {}", value.getValue());
                valueFromServer.set(value.getValue());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info("Completed");
            }
        });

        return channel;
    }
}
