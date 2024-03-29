package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;
import ru.otus.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class R2DbcMessageService implements MessageService {

    private final MessageRepository messageRepository;


    @Override
    public Mono<Message> saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> findMessagesByRoomId(String roomId) {
        return messageRepository.findByRoomId(roomId);
    }

    @Override
    public Flux<Message> findAllMessages() {
        return messageRepository.findAll();
    }
}
