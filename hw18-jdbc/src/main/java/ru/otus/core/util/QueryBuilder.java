package ru.otus.core.util;

import java.util.List;

public interface QueryBuilder {
    String selectAll(String table);

    String selectById(String table, Object id);

    String insert(String table, List<String> fields);

    String update(String table, String fieldIdName, List<String> fields);

    String deleteAll(String table);
}
