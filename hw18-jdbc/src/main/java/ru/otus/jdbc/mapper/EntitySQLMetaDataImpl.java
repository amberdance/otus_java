package ru.otus.jdbc.mapper;


import ru.otus.core.util.QueryBuilder;

import java.lang.reflect.Field;


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
        return queryBuilder.selectAll(table);
    }

    @Override
    public String getSelectByIdSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInsertSql() {
        return queryBuilder.insert(table, metaData.getFieldsWithoutId().stream().map(Field::getName).toList());
    }

    @Override
    public String getUpdateSql() {
        throw new UnsupportedOperationException();
    }

}
