package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;

public interface MessageService {

    Mono<Message> saveMessage(Message message);

    Flux<Message> findMessagesByRoomId(String roomId);

    Flux<Message> findAllMessages();
}
