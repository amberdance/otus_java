package ru.otus.core.util;

import java.util.List;

public class SqlQueryBuilder implements QueryBuilder {
    public static final String SELECT_ALL = "SELECT *";
    public static final String INSERT = "INSERT INTO ";
    public static final String UPDATE = "UPDATE ";
    public static final String DELETE = "DELETE ";
    public static final String FROM = " FROM ";
    public static final String WHERE = " WHERE ";
    public static final String SET = " SET ";
    public static final String VALUES = " VALUES ";
    public static final String EQUALS = " = ";
    public static final String EOL = ";";
    private final StringBuilder query = new StringBuilder();

    @Override
    public String selectAll(String table) {
        flushQuery();

        query.append(SELECT_ALL)
                .append(FROM)
                .append(table);

        return buildQuery();
    }

    @Override
    public String selectById(String table, Object id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String insert(String table, List<String> fields) {
        flushQuery();

        query.append(INSERT)
                .append(table)
                .append(formatFields(fields))
                .append(VALUES)
                .append(formatPlaceholders(fields));

        return buildQuery();
    }


    @Override
    public String update(String table, Object id, List<String> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String deleteAll(String table) {
        flushQuery();

        query.append(DELETE)
                .append(FROM)
                .append(table);

        return buildQuery();
    }

    private String buildQuery() {
        query.append(EOL);

        return query.toString();
    }

    private void flushQuery() {
        query.setLength(0);
    }

    private String formatFields(List<String> fields) {
        return " (" + String.join(", ", fields) + ")";
    }

    private String formatPlaceholders(List<String> fields) {
        var size = fields.size();

        return size == 1 ? "(?)" : "(" + "?, ".repeat(fields.size() - 1) + "?)";
    }
}
