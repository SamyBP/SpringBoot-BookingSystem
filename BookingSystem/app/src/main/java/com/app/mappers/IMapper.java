package com.app.mappers;

import java.lang.reflect.Field;

public interface IMapper {
    default  <S, T> T createMap(S source, Class<T> clazz) {
        T destination = null;

        try {
            destination = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Field[] sourceFields = source.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            try {
                sourceField.setAccessible(true);
                Field destiantionField = clazz.getDeclaredField(sourceField.getName());
                destiantionField.setAccessible(true);
                destiantionField.set(destination, sourceField.get(source));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return destination;
    }
}
