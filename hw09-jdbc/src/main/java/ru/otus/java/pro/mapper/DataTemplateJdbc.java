package ru.otus.java.pro.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.otus.java.pro.core.annotation.Id;
import ru.otus.java.pro.core.repository.DataTemplate;
import ru.otus.java.pro.core.repository.DataTemplateException;
import ru.otus.java.pro.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> type) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = type;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String ps = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, ps, List.of(id), this::handleResult);
    }

    @Override
    public List<T> findAll(Connection connection) {
        String ps = entitySQLMetaData.getInsertSql();
        return dbExecutor
                .executeSelect(connection, ps, List.of(), this::handleResults)
                .orElseThrow();
    }

    @Override
    public long insert(Connection connection, T client) {
        String ps = entitySQLMetaData.getInsertSql();
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        return field.get(client);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        return dbExecutor.executeStatement(connection, ps, params);
    }

    @Override
    public void update(Connection connection, T client) {
        String ps = entitySQLMetaData.getUpdateSql();
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        return field.get(client);
                    } catch (IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .toList();
        try {
            params.add(entityClassMetaData.getIdField().get(client));
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
        dbExecutor.executeStatement(connection, ps, params);
    }

    private T handleResult(ResultSet rs) {
        List<T> results = handleResults(rs);
        return results.isEmpty() ? null : results.get(0);
    }

    private List<T> handleResults(ResultSet rs) {
        List<T> out = new ArrayList<>();
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        constructor.setAccessible(true);
        try {
            while (rs.next()) {
                T instance = constructor.newInstance();
                for (Field field : entityClassMetaData.getAllFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.set(instance, rs.getObject("id"));
                    } else {
                        field.set(instance, rs.getObject(field.getName().toLowerCase()));
                    }
                }
                out.add(instance);
            }
            return out;
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    //    private List<Object> getParams(List<Field> fields){
    //
    //    }
}
