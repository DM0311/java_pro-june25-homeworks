package ru.otus.java.pro.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entitySQLMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaDataClient) {
        this.entitySQLMetaData = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entitySQLMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from " + entitySQLMetaData.getName() + " where id = ?";
    }

    @Override
    public String getInsertSql() {
        StringBuilder values = new StringBuilder("(");
        StringBuilder parameters = new StringBuilder("(");
        List<Field> fieldsWithoutId = entitySQLMetaData.getFieldsWithoutId();
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            values.append(fieldsWithoutId.get(i).getName());
            parameters.append("?");
            if (i < fieldsWithoutId.size() - 1) {
                values.append(",");
                parameters.append(",");
            }
        }
        values.append(")");
        parameters.append(")");
        StringBuilder sql = new StringBuilder("insert into " + entitySQLMetaData.getName() + "");
        sql.append(values);
        sql.append(" values ");
        sql.append(parameters);
        return sql.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }
}
