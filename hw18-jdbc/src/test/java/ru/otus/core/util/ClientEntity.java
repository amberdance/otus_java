package ru.otus.core.util;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.annotation.Table;

@Table(name = "clients")
public class ClientEntity {
    @Id
    private final String id;
    private final String name;

    public ClientEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


}
