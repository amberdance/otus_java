package ru.otus.protobuf.client;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.generated.NumberMessage;

@RequiredArgsConstructor
@Slf4j
public class NumberMessageStreamObserver implements StreamObserver<NumberMessage> {


    private final NumberProcessor numberProcessor;

    @Override
    public void onNext(NumberMessage numberMessage) {
        numberProcessor.setReceived(numberMessage.getNumber());
    }

    @Override
    public void onError(Throwable e) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void onCompleted() {
        log.info("Observe completed");
    }
}
