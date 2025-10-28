package ru.otus.java.pro.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.otus.java.pro.core.annotation.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = initConstructor();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> allFieldsList = new ArrayList<>();
        List<Field> noIdFieldsList = new ArrayList<>();
        Field foundIdField = null;

        for (Field field : fields) {
            field.setAccessible(true);
            allFieldsList.add(field);

            if (field.isAnnotationPresent(Id.class)) {
                if (foundIdField != null) {
                    throw new RuntimeException("Multiple @Id annotations found in " + clazz.getName());
                }
                foundIdField = field;
            } else {
                noIdFieldsList.add(field);
            }
        }

        this.allFields = Collections.unmodifiableList(allFieldsList);
        this.fieldsWithoutId = Collections.unmodifiableList(noIdFieldsList);
        if (foundIdField == null) {
            throw new RuntimeException("No field with @Id annotation found in " + clazz.getName());
        }
        this.idField = foundIdField;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            throw new RuntimeException("No field with @Id annotation found in " + clazz.getName());
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Constructor<T> initConstructor() {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor found in " + clazz.getName(), e);
        }
    }
}
