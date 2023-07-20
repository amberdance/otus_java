package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import ru.otus.protobuf.generated.NumberGeneratorGrpc;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.RunMessage;

@RequiredArgsConstructor
public class NumberGeneratorGrpcImpl extends NumberGeneratorGrpc.NumberGeneratorImplBase {

    private final NumberGeneratorService numberGeneratorService;

    @Override
    public void generate(RunMessage request, StreamObserver<NumberMessage> responseObserver) {
        numberGeneratorService.generate(
                request.getFrom(),
                request.getTo(),
                (number) -> responseObserver.onNext(
                        NumberMessage.newBuilder()
                                .setNumber(number)
                                .build()
                )
        );

        responseObserver.onCompleted();
    }
}
