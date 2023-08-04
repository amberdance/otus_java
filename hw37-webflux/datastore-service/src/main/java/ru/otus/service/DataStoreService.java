package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;

public interface DataStoreService {

    Mono<Message> saveMessage(Message message);

    Flux<Message> findMessageByRoomId(String roomId);

    Flux<Message> findAllMessages(String roomId);
}
