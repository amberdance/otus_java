package ru.otus.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.domain.MessageDto;
import ru.otus.service.MessageService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;
    private final Scheduler workerPool;


    @PostMapping(value = "/msg/{roomId}")
    public Mono<Long> messageFromChat(@PathVariable("roomId") String roomId,
                                      @RequestBody MessageDto messageDto) {
        var message = messageDto.message();
        var msgId = Mono.just(new Message(roomId, message))
                .doOnNext(msg -> log.info("Got message: {}", msg))
                .flatMap(messageService::saveMessage)
                .publishOn(workerPool)
                .doOnNext(msgSaved -> log.info("Message saved with id: {}", msgSaved.getId()))
                .map(Message::getId)
                .subscribeOn(workerPool);

        log.info("Got message from chat, room Id: {}, Message: {}", roomId, message);

        return msgId;
    }

    @GetMapping(value = "/msg/{roomId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessagesByRoomId(@PathVariable("roomId") String roomId) {
        return Mono.just(roomId)
                .doOnNext(room -> log.info("Got message by room id: {}", room))
                .flatMapMany(messageService::findMessagesByRoomId)
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("Message: {}", msgDto))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/msg/all", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getAllMassages() {
        return messageService.findAllMessages()
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("All messages: {}", msgDto))
                .subscribeOn(workerPool);
    }
}
