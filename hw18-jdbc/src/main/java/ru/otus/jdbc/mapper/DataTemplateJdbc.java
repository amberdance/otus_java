package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.util.EntityReflection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final static Logger logger = LoggerFactory.getLogger(DataTemplateJdbc.class);
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;

        //TODO: Обыграть внедрение этой зависимости
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public List<T> findAll(Connection connection) {
        var query = entitySQLMetaData.getSelectAllSql();
        logger.debug(query);

        return dbExecutor.executeSelect(connection, query, Collections.emptyList(), rs -> {
            var result = new ArrayList<T>();

            try {
                while (rs.next()) {
                    //TODO: Грязно, рефлексия + логика работы с бд, надо обыграть
                    var entity = entityClassMetaData.getConstructor().newInstance();
                    var fields = entityClassMetaData.getAllFields();

                    //TODO: То же самое
                    for (var field : fields) {
                        field.setAccessible(true);
                        field.set(entity, rs.getObject(field.getName()));
                    }

                    result.add(entity);
                }

                return result;

            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Something was wrong while fetching data"));
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(Connection connection, T entity) {
        var query = entitySQLMetaData.getInsertSql();
        var params = EntityReflection.getFieldValues(entity, entityClassMetaData.getIdField().getName());
        logger.debug(query);

        try {
            return dbExecutor.executeStatement(connection, query, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        throw new UnsupportedOperationException();
    }

}
