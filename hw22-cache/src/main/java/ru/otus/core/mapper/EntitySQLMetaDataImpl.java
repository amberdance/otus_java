package ru.otus.core.mapper;

import ru.otus.core.mapper.EntityClassMetaData;
import ru.otus.core.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> classMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMetaData) {
        this.classMetaData = classMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return select().toString();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder selectBuilder = select();
        selectBuilder.append(" where ");
        selectBuilder.append(classMetaData.getIdField().getName());
        selectBuilder.append(" = ?");
        return selectBuilder.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder insertBuilder = new StringBuilder("insert into ");
        insertBuilder.append(classMetaData.getName());
        insertBuilder.append("(");
        StringBuilder valuesBuilder = new StringBuilder(" values (");
        List<Field> fieldsWithoutId = classMetaData.getFieldsWithoutId();
        for (int i = 0; i < fieldsWithoutId.size() - 1; i++) {
            Field field = fieldsWithoutId.get(i);
            insertBuilder.append(field.getName());
            insertBuilder.append(", ");
            valuesBuilder.append("?, ");
        }
        insertBuilder.append(fieldsWithoutId.get(fieldsWithoutId.size() - 1).getName());
        valuesBuilder.append("?)");
        insertBuilder.append(")");
        insertBuilder.append(valuesBuilder);
        return insertBuilder.toString();

    }

    @Override
    public String getUpdateSql() {
        StringBuilder updateBuilder = new StringBuilder("update ");
        updateBuilder.append(classMetaData.getName());
        updateBuilder.append(" set ");
        List<Field> fieldsWithoutId = classMetaData.getFieldsWithoutId();
        for (int i = 0; i < fieldsWithoutId.size() - 1; i++) {
            Field field = fieldsWithoutId.get(i);
            updateBuilder.append(field.getName());
            updateBuilder.append(" = ?, ");
        }
        updateBuilder.append(fieldsWithoutId.get(fieldsWithoutId.size() - 1).getName());
        updateBuilder.append(" = ? where ");
        updateBuilder.append(classMetaData.getIdField().getName());
        updateBuilder.append(" = ?");
        return updateBuilder.toString();
    }

    private StringBuilder select() {
        StringBuilder selectBuilder = new StringBuilder("select ");
        List<Field> allFields = classMetaData.getAllFields();
        for (int i = 0; i < allFields.size() - 1; i++) {
            Field field = allFields.get(i);
            selectBuilder.append(field.getName());
            selectBuilder.append(", ");
        }
        selectBuilder.append(allFields.get(allFields.size() - 1).getName());
        selectBuilder.append(" from ");
        selectBuilder.append(classMetaData.getName());
        return selectBuilder;
    }
}
