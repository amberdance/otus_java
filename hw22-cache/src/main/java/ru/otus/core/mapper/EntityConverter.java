package ru.otus.core.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface EntityConverter<T> {

    T convert(ResultSet resultSet);

    List<T> convertAll(ResultSet resultSet);
}
