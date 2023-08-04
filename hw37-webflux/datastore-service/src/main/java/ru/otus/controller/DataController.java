package ru.otus.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.domain.MessageDto;
import ru.otus.service.DataStoreService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {
    private final DataStoreService dataStoreService;
    private final Scheduler workerPool;


    @PostMapping(value = "/msg/{roomId}")
    public Mono<Long> messageFromChat(@PathVariable("roomId") String roomId,
                                      @RequestBody MessageDto messageDto) {
        var message = messageDto.message();
        var msgId = Mono.just(new Message(roomId, message))
                .doOnNext(msg -> log.info("messageFromChat:{}", msg))
                .flatMap(dataStoreService::saveMessage)
                .publishOn(workerPool)
                .doOnNext(msgSaved -> log.info("msgSaved id:{}", msgSaved.getId()))
                .map(Message::getId)
                .subscribeOn(workerPool);

        log.info("messageFromChat, roomId:{}, msg:{} done", roomId, message);

        return msgId;
    }

    @GetMapping(value = "/msg/{roomId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessagesByRoomId(@PathVariable("roomId") String roomId) {
        return Mono.just(roomId)
                .doOnNext(room -> log.info("getMessagesByRoomId, room:{}", room))
                .flatMapMany(dataStoreService::loadMessages)
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/msg/all", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getAllMassages() {
        return Mono.just("all")
                .doOnNext(room -> log.info("getMessagesByRoomId, room:{}", room))
                .flatMapMany(dataStoreService::loadAllMessages)
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                .subscribeOn(workerPool);
    }
}
