package ru.otus.jdbc.mapper;


import ru.otus.core.util.QueryBuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> metaData;
    private final QueryBuilder queryBuilder;
    private final String table;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData, QueryBuilder queryBuilder) {
        this.metaData = metaData;
        this.queryBuilder = queryBuilder;
        this.table = metaData.getName();
    }

    @Override
    public String getSelectAllSql() {
        return queryBuilder.select(mapFieldsToStrings(metaData.getAllFields()))
                .from(table)
                .build();
    }

    @Override
    public String getSelectByIdSql() {
        return queryBuilder.select(mapFieldsToStrings(metaData.getAllFields()))
                .from(table)
                .where(Collections.singletonList(metaData.getIdField().getName()));
    }

    @Override
    public String getInsertSql() {
        return queryBuilder.insert(table, Arrays.stream(mapFieldsToStrings(metaData.getFieldsWithoutId())).toList());
    }

    @Override
    public String getUpdateSql() {
        return queryBuilder.update(table, metaData.getIdField().getName(),
                        Arrays.stream(mapFieldsToStrings(metaData.getFieldsWithoutId())).toList())
                .where(Collections.emptyList());
    }

    private String[] mapFieldsToStrings(List<Field> fields) {
        return fields.stream().map(Field::getName).toArray(String[]::new);
    }

}
