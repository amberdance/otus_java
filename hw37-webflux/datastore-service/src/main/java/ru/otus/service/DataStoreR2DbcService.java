package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.repository.MessageRepository;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataStoreR2DbcService implements DataStoreService {

    private final MessageRepository messageRepository;
    private final Scheduler workerPool;


    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);

        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);

        return messageRepository.findByRoomId(roomId).delayElements(Duration.of(1, SECONDS), workerPool);
    }

    @Override
    public Flux<Message> loadAllMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);

        return messageRepository.findAll().delayElements(Duration.of(1, SECONDS), workerPool);
    }
}
