package ru.otus.core.mapper;



import ru.otus.core.exceptions.DataTemplateException;
import ru.otus.core.mapper.DataTemplate;
import ru.otus.core.mapper.EntitySQLMetaData;
import ru.otus.core.mapper.EntityConverter;
import ru.otus.core.mapper.ParamsProvider;
import ru.otus.core.executor.DbExecutor;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final ParamsProvider<T> paramsProvider;

    private final EntityConverter<T> entityConverter;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            ParamsProvider<T> paramsProvider,
            EntityConverter<T> entityConverter
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.paramsProvider = paramsProvider;
        this.entityConverter = entityConverter;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                Collections.singletonList(id),
                entityConverter::convert
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                entityConverter::convertAll
        ).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), paramsProvider.getInsertParams(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), paramsProvider.getUpdateParams(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
