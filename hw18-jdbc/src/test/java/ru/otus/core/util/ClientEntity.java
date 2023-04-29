package ru.otus.core.util;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.annotation.Table;
import ru.otus.jdbc.mapper.Entity;

@Table(name = "clients")
public class ClientEntity implements Entity {
    @Id
    private String id;
    private String name;

    public ClientEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
}
