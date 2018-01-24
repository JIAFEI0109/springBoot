package com.cqabj.springboot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象操作
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.MODULE)
public class ObjectUtil {

    /**
     * 对象类型轻质转换
     * @param obj Object对象
     * @param <T> 泛型轻质转换
     * @return null 或者具体的对象
     * @throws ClassCastException 类型转换异常
     */
    public static <T> T typeConversion(Object obj) {
        return obj == null ? null : (T) obj;
    }

    public static <T> T objectToType(Object obj, Class entityClass) {
        if (obj == null) {
            return null;
        }
        String temp = String.valueOf(obj);
        switch (entityClass.getName()) {
            case "java.lang.String":
                return typeConversion(temp);
            case "java.lang.Long":
                return typeConversion(temp);
            case "java.lang.Integer":
                return typeConversion(temp);
            case "java.lang.Double":
                return typeConversion(temp);
            case "java.lang.Boolean":
                return typeConversion(temp);
            case "java.util.Date":
                return typeConversion(temp);
            default:
                return null;
        }
    }
}
