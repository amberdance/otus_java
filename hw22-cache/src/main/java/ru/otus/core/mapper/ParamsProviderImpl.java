package ru.otus.core.mapper;

import ru.otus.core.exceptions.MetaDataException;
import ru.otus.core.mapper.EntityClassMetaData;
import ru.otus.core.mapper.ParamsProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ParamsProviderImpl<T> implements ParamsProvider<T> {

    private final EntityClassMetaData<T> entityClassMetaData;

    public ParamsProviderImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    public List<Object> getInsertParams(T t) {
        try {
            List<Object> list = new ArrayList<>();
            for (Field f : entityClassMetaData.getFieldsWithoutId()) {
                f.setAccessible(true);
                list.add(f.get(t));
            }
            return list;
        } catch (Exception e) {
            throw new MetaDataException(e);
        }
    }

    @Override
    public List<Object> getUpdateParams(T t) {
        try {
            List<Object> params = getInsertParams(t);
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            params.add(idField.get(t));
            return params;
        } catch (Exception e) {
            throw new MetaDataException(e);
        }
    }
}
