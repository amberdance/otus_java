package ru.otus.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SqlQueryBuilder implements QueryBuilder {

    public static final String EQ = " = ";
    public static final String PLACEHOLDER = "?";
    public static final String EOL = ";";
    private static final Logger log = LoggerFactory.getLogger(SqlQueryBuilder.class);
    private final StringBuilder query = new StringBuilder();

    @Override
    public QueryBuilder select(String... fields) {
        query.append("SELECT ");

        if (fields.length == 0) query.append("*");
        else {
            var len = fields.length - 1;

            for (int i = 0; i < len; i++) {
                query.append(fields[i]).append(", ");
            }

            query.append(fields[len]);
        }

        return this;
    }

    @Override
    public QueryBuilder from(String table) {
        query.append(" FROM ").append(table);

        return this;
    }

    @Override
    public String where(List<String> fields) {
        query.append(" WHERE ");

        if (fields.size() == 1) {
            query.append(fields.get(0))
                    .append(EQ)
                    .append(PLACEHOLDER);
        } else {
            for (var field : fields) {
                query.append(field)
                        .append(EQ)
                        .append(PLACEHOLDER);
            }
        }

        return build();
    }

    @Override
    public String insert(String table, List<String> fields) {
        query.append("INSERT INTO ")
                .append(table)
                .append(formatInsertParams(fields))
                .append(" VALUES ")
                .append(formatPlaceholders(fields));

        return build();
    }

    @Override
    public QueryBuilder update(String table, String fieldIdName, List<String> fields) {
        query.append("UPDATE ")
                .append(table)
                .append(" SET ")
                .append(formatUpdateArgs(fields));

        return this;
    }

    @Override
    public QueryBuilder delete() {
        query.append("DELETE");

        return this;
    }

    @Override
    public String build() {
        query.append(EOL);
        var res = query.toString();

        flushQuery();
        log.debug(res);

        return res;
    }

    private void flushQuery() {
        query.setLength(0);
    }

    private String formatInsertParams(List<String> fields) {
        return " (" + String.join(", ", fields) + ")";
    }

    private String formatPlaceholders(List<String> fields) {
        var size = fields.size();

        return size == 1 ? "(?)" : "(" + "?, ".repeat(fields.size() - 1) + "?)";
    }

    private String formatUpdateArgs(List<String> params) {
        var size = params.size();

        if (size == 1) {
            return params.get(0) + " = " + PLACEHOLDER;
        }

        var ans = new StringBuilder();

        for (String param : params) {
            ans.append(", ")
                    .append(param)
                    .append(EQ)
                    .append(PLACEHOLDER);
        }

        ans.delete(0, 2);

        return ans.toString();
    }
}
