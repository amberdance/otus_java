package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.Message;
import ru.otus.exception.ChatException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private static final String TOPIC_TEMPLATE = "/topic/response.";
    private static final long ROOM_1408 = 1408;
    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;


    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable String roomId, Message message) {
        log.info("get message:{}, roomId:{}", message, roomId);

        if (parseRoomId(roomId) == ROOM_1408) {
            log.info("Messages from room {} are prohibited", ROOM_1408);
            return;
        }

        saveMessage(roomId, message).subscribe(msgId -> log.info("message send id:{}", ROOM_1408));
        saveMessage(roomId, message).subscribe(msgId -> log.info("message send id:{}", msgId));

        template.convertAndSend(String.format("%s%s", TOPIC_TEMPLATE, roomId),
                new Message(HtmlUtils.htmlEscape(message.msg())));
    }


    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");

        if (simpDestination == null) {
            log.error("Can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }

        var roomId = parseRoomId(simpDestination);

        if (roomId == ROOM_1408) {
            getMessagesForAllRooms().doOnError(ex -> log.error("getting messages for roomId:{} failed", roomId, ex))
                    .subscribe(message -> template.convertAndSend(simpDestination, message));
        } else {
            getMessagesByRoomId(roomId).doOnError(ex -> log.error("getting messages for roomId:{} failed", roomId, ex))
                    .subscribe(message -> template.convertAndSend(simpDestination, message));
        }
    }

    private long parseRoomId(String simpDestination) {
        try {
            return Long.parseLong(simpDestination.replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            log.error("Can not get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient.post().uri(String.format("/msg/%s", roomId)).accept(MediaType.APPLICATION_JSON)
                .bodyValue(message).exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Object> getMessagesForAllRooms() {
        return datastoreClient.get().uri("/msg/all").accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }

    private Flux<Message> getMessagesByRoomId(long roomId) {
        return datastoreClient.get().uri(String.format("/msg/%s", roomId)).accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }
}
