package ru.otus.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table("message")
@Getter
@Setter
@ToString
public class Message {

    @Id
    private final Long id;

    @NonNull
    private final String roomId;

    @NonNull
    private final String msgText;

    @PersistenceCreator
    public Message(Long id, @NonNull String roomId, @NonNull String msgText) {
        this.id = id;
        this.roomId = roomId;
        this.msgText = msgText;
    }

    public Message(@NonNull String roomId, @NonNull String msgText) {
        this(null, roomId, msgText);
    }

}
