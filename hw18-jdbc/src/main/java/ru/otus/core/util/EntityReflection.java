package ru.otus.core.util;

import ru.otus.jdbc.annotation.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityReflection<T> {

    private final Class<T> theClass;

    public EntityReflection(Class<T> entity) {
        this.theClass = entity;
    }

    public static List<Object> getFieldValues(Object instance, String... excludedFields) {
        var result = new ArrayList<>();
        var excludedFieldsList = Arrays.stream(excludedFields).toList();

        for (var field : instance.getClass().getDeclaredFields()) {
            if (excludedFieldsList.contains(field.getName())) continue;

            try {
                field.setAccessible(true);
                result.add(field.get(instance));
            } catch (IllegalAccessException | SecurityException | InaccessibleObjectException e) {
                throw new RuntimeException("Cannot get value of field: " + field.getName());
            }
        }

        return result;
    }

    public String getTableName() {
        try {
            return theClass.getDeclaredAnnotationsByType(Table.class)[0].name();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return theClass.getSimpleName().toLowerCase();
        }
    }

    public Constructor<T> getConstructor(Class<?>... parameterTypes) {
        try {
            return theClass.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot get constructor of " + theClass.getName());
        }
    }

    public List<Field> getFields() {
        return Arrays.stream(theClass.getDeclaredFields()).toList();
    }

    public List<Field> getFieldsExclude(String excludeField) {
        return getFields().stream().filter(field -> !field.getName().equals(excludeField)).toList();
    }

    public Field getIdField() {
        return Arrays.stream(theClass.getDeclaredFields()).findFirst().orElseThrow(() ->
                new NullPointerException("Make sure that " + theClass.getName() + " has one field with annotation @Id"));
    }

    public Object getFieldValue(Field field, Object instance) {
        try {
            field.setAccessible(true);

            return field.get(instance);
        } catch (IllegalAccessException | SecurityException | InaccessibleObjectException e) {
            throw new RuntimeException("Cannot get value of field: " + field.getName());
        }
    }

}
