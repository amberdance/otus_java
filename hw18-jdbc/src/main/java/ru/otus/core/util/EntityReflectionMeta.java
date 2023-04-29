package ru.otus.core.util;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.annotation.Table;
import ru.otus.jdbc.mapper.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityReflectionMeta {

    private final Class<? extends Entity> type;

    public EntityReflectionMeta(Class<? extends Entity> type) {
        this.type = type;
    }

    public List<Field> getFieldsExclude(String excludeField) {
        return getFields().stream().filter(field -> !field.getName().equals(excludeField)).toList();
    }

    public String getTableName() {
        try {
            return type.getDeclaredAnnotationsByType(Table.class)[0].name();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return type.getSimpleName().toLowerCase();
        }
    }

    public Field getIdField() {
        for (var field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) return field;
        }

        throw new NullPointerException("Make sure that " + type.getSimpleName() + " has one field with annotation @Id");
    }

    public Object getIdFieldValue(Field idField, Object instance) throws IllegalAccessException {
        idField.setAccessible(true);
        return idField.get(instance);
    }

    public Constructor<? extends Entity> getConstructor(Class<?>... parameterTypes) throws NoSuchMethodException {
        return type.getConstructor(parameterTypes);
    }

    public List<Field> getFields() {
        return Arrays.stream(type.getDeclaredFields()).toList();
    }

}
