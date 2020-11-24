package utils;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举类工具类
 *
 * @author Funchs
 * @date 2018-02-02 14:50
 **/
public class EnumUtils {

    public static <T> T valueOfKey(Class<T> enumClass, String keyName, String keyValue) throws InvocationTargetException, IllegalAccessException {
        if (!enumClass.isEnum()) {
            return null;
        }
        T[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return null;
        }
        List<String> fieldNames = getFieldNames(enumClass);
        if (fieldNames.isEmpty()) {
            return null;
        }
        Method[] methods = enumClass.getMethods();
        if (ArrayUtils.isEmpty(methods)) {
            return null;
        }
        for (T enumT : enums) {
            Map<String, Object> map = getMap(fieldNames, methods, enumT);
            String key = String.valueOf(map.get(keyName));
            if (key.equals(keyValue)) {
                return enumT;
            }
        }

        return null;
    }

    /**
     * 枚举转map集合
     *
     * @param enumClass 枚举类
     * @return enum
     */
    public static <T> List<Map<String, Object>> enumToList(Class<T> enumClass) throws InvocationTargetException, IllegalAccessException {
        List<Map<String, Object>> enumList = new ArrayList<>();
        if (!enumClass.isEnum()) {
            return enumList;
        }
        T[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enumList;
        }
        List<String> fieldNames = getFieldNames(enumClass);
        if (fieldNames.isEmpty()) {
            return enumList;
        }
        Method[] methods = enumClass.getMethods();
        if (ArrayUtils.isEmpty(methods)) {
            return enumList;
        }
        for (T enumT : enums) {
            Map<String, Object> map = getMap(fieldNames, methods, enumT);
            if (!map.isEmpty()) {
                enumList.add(map);
            }
        }
        return enumList;
    }

    private static List<String> getFieldNames(Class enumClass) {
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = enumClass.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            return fieldNames;
        }
        for (Field field : fields) {
            if (!(field.getType().getSimpleName().contains(enumClass.getSimpleName()))) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    /**
     * 根据反射，通过属性名称获取方法值，忽略大小写的
     *
     * @param fieldNames 属性名称
     * @param methods    方法列表
     * @return map
     */
    private static <T> Map<String, Object> getMap(List<String> fieldNames, Method[] methods, T enumT) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> enumMap = new HashMap<>(16);
        if (fieldNames.isEmpty()) {
            return enumMap;
        }
        if (ArrayUtils.isEmpty(methods)) {
            return enumMap;
        }
        for (String fieldName : fieldNames) {
            for (Method method : methods) {
                if (!method.getName().equalsIgnoreCase("get" + fieldName)) {
                    continue;
                }
                enumMap.put(fieldName, null == method.invoke(enumT) ? "" : method.invoke(enumT));
            }
        }
        return enumMap;
    }
}

