package ru.otus.core.util;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.annotation.Table;

@Table(name = "managers")
public class ManagerEntity {
    @Id
    private final long identifier;
    private final String name;
    private final String someParam;

    public ManagerEntity(long id, String name, String someParam) {
        this.identifier = id;
        this.name = name;
        this.someParam = someParam;
    }

    public String getName() {
        return name;
    }

    public String getSomeParam() {
        return someParam;
    }

    public long getId() {
        return identifier;
    }
}
