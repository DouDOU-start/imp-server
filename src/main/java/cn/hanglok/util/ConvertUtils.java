package cn.hanglok.util;

import lombok.SneakyThrows;
import java.lang.reflect.Field;

/**
 * @author Allen
 * @version 1.0
 * @className ConvertUtils
 * @description TODO
 * @date 2023/6/8 16:43
 */
public class ConvertUtils {

    /**
     * 实体类转DTO
     * @param entity 实体类
     * @param dto DTO
     * @return DTO
     * @param <T> 实体类
     * @param <U> DTO
     */
    @SneakyThrows
    public static <T, U> U entityToDto(T entity, Class<T> entityClass, Class<U> dto) {

        U bean = dto.getDeclaredConstructor().newInstance();

        Field[] entityFields = entityClass.getDeclaredFields();
        Field[] dtoFields = dto.getSuperclass().getDeclaredFields();

        for (Field entityField : entityFields) {
            entityField.setAccessible(true);
            if ("serialVersionUID".equals(entityField.getName())) {
                continue;
            }
            for (Field dtoField : dtoFields) {
                dtoField.setAccessible(true);
                if (entityField.getName().equals(dtoField.getName())) {
                    dtoField.set(bean, entityField.get(entity));
                    break;
                }
            }
        }

        return bean;
    }

    @SneakyThrows
    public static <T, U> U dtoToEntity(T dto, Class<T> dtoClass, Class<U> entity) {

        U bean = entity.getDeclaredConstructor().newInstance();

        Field[] dtoFields = dtoClass.getSuperclass().getDeclaredFields();
        Field[] entityFields = entity.getDeclaredFields();

        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true);
//            if ("serialVersionUID".equals(entityField.getName())) {
//                continue;
//            }
            for (Field entityField : entityFields) {
                entityField.setAccessible(true);
                if (dtoField.getName().equals(entityField.getName())) {
                    dtoField.set(bean, entityField.get(entity));
                    break;
                }
            }
        }

        return bean;
    }
}
