package ru.otus.java.pro.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ru.otus.java.pro.core.annotation.Id;
import ru.otus.java.pro.core.repository.DataTemplate;
import ru.otus.java.pro.core.repository.DataTemplateException;
import ru.otus.java.pro.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
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
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        constructor.setAccessible(true);

        return dbExecutor.executeSelect(connection, ps, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T instance = constructor.newInstance();
                    for (Field field : entityClassMetaData.getAllFields()) {
                        try {
                            Field f = instance.getClass().getDeclaredField(field.getName());
                            f.setAccessible(true);
                            if (f.equals(entityClassMetaData.getIdField())) {
                                String fieldName = entityClassMetaData
                                        .getIdField()
                                        .getDeclaredAnnotation(Id.class)
                                        .annotationType()
                                        .getSimpleName()
                                        .toLowerCase();
                                f.set(instance, rs.getObject(fieldName));
                            } else {
                                f.set(instance, rs.getObject(field.getName()));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new DataTemplateException(e);
                        }
                    }
                    return instance;
                }
                return null;
            } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {

        var ps = entitySQLMetaData.getSelectAllSql();
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        constructor.setAccessible(true);

        return dbExecutor.executeSelect(connection, ps, Collections.emptyList(), resultSet -> {
                    List<T> results = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            T instance = constructor.newInstance();
                            for (Field field : entityClassMetaData.getAllFields()) {
                                try {
                                    Field f = instance.getClass().getDeclaredField(field.getName());
                                    f.setAccessible(true);
                                    if (f.equals(entityClassMetaData.getIdField())) {
                                        String fieldName = entityClassMetaData
                                                .getIdField()
                                                .getDeclaredAnnotation(Id.class)
                                                .annotationType()
                                                .getSimpleName()
                                                .toLowerCase();
                                        f.set(instance, resultSet.getObject(fieldName));
                                    } else {
                                        f.set(instance, resultSet.getObject(field.getName()));
                                    }
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    throw new DataTemplateException(e);
                                }
                            }
                            results.add(instance);
                        }
                        return results;
                    } catch (SQLException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        String ps = entitySQLMetaData.getInsertSql();
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                Field declaredField = client.getClass().getDeclaredField(field.getName());
                declaredField.setAccessible(true);
                params.add(declaredField.get(client));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        return dbExecutor.executeStatement(connection, ps, params);
    }

    @Override
    public void update(Connection connection, T client) {
        var ps = entitySQLMetaData.getUpdateSql();
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                Field declaredField = client.getClass().getDeclaredField(field.getName());
                declaredField.setAccessible(true);
                params.add(declaredField.get(client));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        dbExecutor.executeStatement(connection, ps, params);
    }
}
