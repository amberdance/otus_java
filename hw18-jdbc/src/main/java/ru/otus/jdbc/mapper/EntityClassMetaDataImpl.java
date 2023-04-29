package ru.otus.jdbc.mapper;

import ru.otus.core.util.EntityReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final EntityReflection<T> entityReflection;

    public EntityClassMetaDataImpl(EntityReflection<T> entityReflection) {
        this.entityReflection = entityReflection;
    }

    @Override
    public String getName() {
        return entityReflection.getTableName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return entityReflection.getConstructor();
    }

    @Override
    public Field getIdField() {
        return entityReflection.getIdField();
    }

    @Override
    public List<Field> getAllFields() {
        return entityReflection.getFields();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return entityReflection.getFieldsExclude(getIdField().getName());
    }
}

