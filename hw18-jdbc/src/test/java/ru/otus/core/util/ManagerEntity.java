package ru.otus.core.util;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.annotation.Table;
import ru.otus.jdbc.mapper.Entity;

@Table(name = "managers")
public class ManagerEntity implements Entity {
    @Id
    private long identifier;
    private String name;
    private String someParam;

    public ManagerEntity(long id, String name, String someParam) {
        this.identifier = id;
        this.name = name;
        this.someParam = someParam;
    }

    public long getId() {
        return identifier;
    }
}
