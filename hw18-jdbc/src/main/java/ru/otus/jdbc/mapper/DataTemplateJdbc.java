package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.util.EntityReflection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;

        //TODO: Обойти внедрение этой зависимости
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), resultSet -> {
            var result = new ArrayList<T>();

            try {
                while (resultSet.next()) {
                    assignFields(resultSet);
                }

                return result;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElse(new ArrayList<>());
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), Collections.singletonList(id), resultSet -> {
            try {
                resultSet.next();
                return assignFields(resultSet);
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public long insert(Connection connection, T entity) {
        var params = EntityReflection.getFieldValues(entity, entityClassMetaData.getIdField().getName());

        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        var fields = entityClassMetaData.getFieldsWithoutId();
        var params = new ArrayList<>();

        try {
            for (var field : fields) {
                field.setAccessible(true);
                params.add(field.get(entity));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot get field value");
        }

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    private T assignFields(ResultSet rs) {
        T entity = null;

        try {
            entity = entityClassMetaData.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot instantiate " + entity);
        }

        var fields = entityClassMetaData.getAllFields();

        for (var field : fields) {
            try {
                field.setAccessible(true);
                field.set(entity, rs.getObject(field.getName()));
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return entity;
    }

}
