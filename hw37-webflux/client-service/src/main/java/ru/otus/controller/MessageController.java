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
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.model.Message;

import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private static final String TOPIC_TEMPLATE = "/topic/response.";
    private static final String RESTRICTED_ROOM = "1408";
    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable String roomId, Message message) {
        if (isRoomRestricted(roomId)) {
            return;
        }

        saveMessage(roomId, message).doOnNext(msg -> sendMessage(roomId, message))
                .subscribe(msgId -> log.info("Message sent with id:{}", msgId));
    }

    private boolean isRoomRestricted(String roomId) {
        return roomId.equals(RESTRICTED_ROOM);
    }

    @EventListener
    public void handleSubscribeEventAction(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");

        Objects.requireNonNull(simpDestination);

        var roomId = parseRoomId(simpDestination);
        var fluxMessages = isRoomRestricted(roomId) ? getMessagesForAllRooms() : getMessagesByRoomId(roomId);

        fluxMessages.doOnError(ex -> log.error("Error occurred while getting messages", ex))
                .subscribe(message -> template.convertAndSend(simpDestination, message));
    }

    private String parseRoomId(String simpDestination) {
        return simpDestination.replace(TOPIC_TEMPLATE, "");
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient.post().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class))
                .doOnNext(msg -> sendMessage(RESTRICTED_ROOM, message));
    }

    private Flux<Message> getMessagesForAllRooms() {
        return datastoreClient.get().uri("/msg/all")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> isHttpResponseOk(response) ? response.bodyToFlux(Message.class) :
                        response.createException().flatMapMany(Mono::error));
    }

    private Flux<Message> getMessagesByRoomId(String roomId) {
        return datastoreClient.get().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> isHttpResponseOk(response) ? response.bodyToFlux(Message.class) :
                        response.createException().flatMapMany(Mono::error));
    }

    private boolean isHttpResponseOk(ClientResponse clientResponse) {
        return clientResponse.statusCode().equals(HttpStatus.OK);
    }

    private void sendMessage(String roomId, Message message) {
        template.convertAndSend(String.format("%s%s", TOPIC_TEMPLATE, roomId),
                new Message(HtmlUtils.htmlEscape(message.message())));
    }

}
