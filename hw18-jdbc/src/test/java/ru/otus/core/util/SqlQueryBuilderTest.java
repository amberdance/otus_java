package ru.otus.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlQueryBuilderTest {
    private static final String TABLE = "users";
    private static final String EQL = ";";

    private static final String WHERE_CONDITION = " WHERE id = ?";
    private static final QueryBuilder queryBuilder = new SqlQueryBuilder();

    @Test
    @DisplayName("Выборка всего через *")
    void testSelectAll() {
        final var SELECT_ALL = "SELECT * FROM " + TABLE + EQL;

        assertEquals(SELECT_ALL, queryBuilder.select()
                .from(TABLE)
                .build()
        );
    }

    @Test
    @DisplayName("Выборка всего через перечисление полей")
    void testSelectAllWithNamedFields() {
        final var SELECT_WITH_NAMED_FIELDS = "SELECT id, first_name, last_name FROM " + TABLE + EQL;

        assertEquals(SELECT_WITH_NAMED_FIELDS, queryBuilder.select("id", "first_name", "last_name")
                .from(TABLE)
                .build()
        );
    }

    @Test
    @DisplayName("Выборка всего через перечисление полей с условием where")
    void testSelectWithWhereCondition() {
        final var SELECT_WITH_NAMED_FIELDS_WHERE_CONDITION = "SELECT id, first_name, last_name FROM " + TABLE + WHERE_CONDITION + EQL;

        assertEquals(SELECT_WITH_NAMED_FIELDS_WHERE_CONDITION, queryBuilder.select("id", "first_name", "last_name")
                .from(TABLE)
                .where(Collections.singletonList("id"))
        );
    }

    @Test
    @DisplayName("Вставка")
    void testInsertSql() {
        final var INSERT = "INSERT INTO " + TABLE + " (first_name, last_name)" + " VALUES (?, ?)" + EQL;

        assertEquals(INSERT, queryBuilder.insert(TABLE, List.of("first_name", "last_name")));
    }

    @Test
    @DisplayName("Удаление без условия")
    void testDeleteAll() {
        final var DELETE = "DELETE FROM " + TABLE + EQL;

        assertEquals(DELETE, queryBuilder.delete()
                .from(TABLE)
                .build());
    }

    @Test
    @DisplayName("Удаление с условием")
    void testDeleteWhere() {
        final var DELETE = "DELETE FROM " + TABLE + WHERE_CONDITION + EQL;

        assertEquals(DELETE, queryBuilder.delete()
                .from(TABLE)
                .where(Collections.singletonList("id"))
        );
    }

    @Test
    @DisplayName("Обновление")
    void testUpdate() {
        final var UPDATE = "UPDATE " + TABLE + " SET first_name = ?, last_name = ?" + WHERE_CONDITION + EQL;

        assertEquals(UPDATE, queryBuilder.update(TABLE, "id", List.of("first_name", "last_name")).where(Collections.singletonList("id")));
    }
}