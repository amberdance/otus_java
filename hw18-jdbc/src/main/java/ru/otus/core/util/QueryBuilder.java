package ru.otus.core.util;

import java.util.List;

public interface QueryBuilder {
    QueryBuilder select(String... fields);

    QueryBuilder from(String table);

    String where(List<String> fields);

    String insert(String table, List<String> fields);

    QueryBuilder update(String table, String fieldIdName, List<String> fields);

    QueryBuilder delete();

    String build();
}
