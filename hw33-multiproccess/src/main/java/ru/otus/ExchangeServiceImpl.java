package ru.otus;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ExchangeServiceImpl extends ExchangeServiceGrpc.ExchangeServiceImplBase {
    @Override
    public void send(ExchangeMessageRequest request, StreamObserver<ExchangeMessageResponse> responseObserver) {
        log.info("request.getValue() = {} ", request.getFirstValue());
        log.info("request.getValue() = {}", request.getLastValue());

        var firstValue = request.getFirstValue();
        var lastValue = request.getLastValue();

        for (int counter = firstValue; counter < lastValue; counter++) {
            try {
                var response = ExchangeMessageResponse.newBuilder().setValue(firstValue + counter).build();
                log.info("Send response = " + response);
                responseObserver.onNext(response);
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        responseObserver.onCompleted();
    }
}
