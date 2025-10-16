package ru.otus.java.pro.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import ru.otus.java.pro.core.annotation.Id;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectById;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {

        String tableName = entityClassMetaDataClient.getName().toLowerCase();

        List<String> noIdColumns = entityClassMetaDataClient.getFieldsWithoutId().stream()
                .map(Field::getName)
                .map(String::toLowerCase)
                .toList();
        String idColumnName = entityClassMetaDataClient
                .getIdField()
                .getAnnotation(Id.class)
                .annotationType()
                .getSimpleName()
                .toLowerCase();
        String noIdColumnsNames = String.join(", ", noIdColumns);
        String allColumnsNames = idColumnName + ", " + noIdColumnsNames;
        String params = noIdColumns.stream().map(c -> "?").collect(Collectors.joining(", "));
        String updateValues = noIdColumns.stream().map(c -> c + " = ?").collect(Collectors.joining(", "));

        this.selectAllSql = "select " + allColumnsNames + " from " + tableName;
        this.selectById = "select " + allColumnsNames + " from " + tableName + " where " + idColumnName + " = ?";
        this.insertSql = "insert into " + tableName + " (" + noIdColumnsNames + ") values (" + params + ")";
        this.updateSql = "update " + tableName + " set " + updateValues + " where " + idColumnName + " = ?"; //
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectById;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
