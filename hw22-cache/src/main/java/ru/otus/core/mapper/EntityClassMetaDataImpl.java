package ru.otus.core.mapper;

import ru.otus.core.annotation.Id;
import ru.otus.core.exceptions.MetaDataException;
import ru.otus.core.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        try {
            this.name = getName(clazz);
            this.constructor = getConstructor(clazz);
            this.allField = getAllField(clazz);
            this.fieldsWithoutId = new ArrayList<>(allField.size() - 1);
            this.idField = fillWithoutIdAndGetId(fieldsWithoutId, allField);
        } catch (Exception e) {
            throw new MetaDataException(e);
        }
    }

    private Field fillWithoutIdAndGetId(List<Field> fieldsWithoutId, List<Field> allField) {
        Field tempIdField = null;
        for (Field field : allField) {
            if (field.getAnnotation(Id.class) != null) {
                if (tempIdField == null) {
                    tempIdField = field;
                } else {
                    throw new MetaDataException("Должно быть только одно поле с аннотацией Id");
                }
            } else {
                fieldsWithoutId.add(field);
            }
        }
        if (tempIdField == null) {
            throw new MetaDataException("Хотя бы одно поле должно быть помечено аннотацией Id");
        }
        return tempIdField;
    }

    private List<Field> getAllField(Class<T> clazz) {
        return List.of(clazz.getDeclaredFields());
    }

    private Constructor<T> getConstructor(Class<T> clazz) throws NoSuchMethodException {
        return clazz.getDeclaredConstructor();
    }

    private String getName(Class<T> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allField;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
