package com.kyle.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class BeanUtils {

    private static final Map<Class, Map<String, Method>> SET_METHOD_MAP = new HashMap<>();
    private static final Map<Class, Map<String, Method>> GET_METHOD_MAP = new HashMap<>();
    private static final Map<Class, List<Field>> FIELDS_MAP = new HashMap<>();

    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 属性拷贝：以descObj为准，从orgObj中获取相同属性名和类型的属性设置到descObj对象中，如果复制属性时出现异常则跳过
     *
     * @param orgObj            院对象
     * @param destObj           目标对象
     * @param excludeProperties 忽略的属性集合
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void copyPropertiesIgnorException(Object orgObj, Object destObj, List<String> excludeProperties) {
        if (orgObj == null || destObj == null) {
            return;
        }

        Class orgClass = orgObj.getClass();
        Class destClass = destObj.getClass();
        String fieldName = null;
        Method getMethod = null;
        Method setMethod = null;

        Object value = null;
        Object descValue = null;
        StringBuilder ignoreFields = new StringBuilder();
        Class clazz = destClass;
        for (Field field : getClassFields(clazz)) {
            fieldName = field.getName();
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (fieldName != null && "MetaDataMap".equalsIgnoreCase(fieldName)) {
                continue;
            }
            if (excludeProperties != null && excludeProperties.size() > 0 && excludeProperties.contains(fieldName)) {
                continue;
            }
            fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                getMethod = getGetMethod(orgClass, fieldName);
                if (getMethod == null) {
                    ignoreFields.append(fieldName).append(",   ");
                    continue;
                }
                value = getMethod.invoke(orgObj);
                if (value == null) {
                    continue;
                }

                if ("boolean".equalsIgnoreCase(field.getType().getName()) && field.getName().startsWith("is") && fieldName.length() > 2) {
                    String tmpFieldName = fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3);
                    String tmpMethodKey = String.format("set%s", tmpFieldName);
                    if (!getSetMethods(destClass).containsKey(tmpMethodKey)) {
                        try {
                            setMethod = destClass.getMethod("set" + fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3), field.getType());
                            getSetMethods(destClass).put(tmpMethodKey, setMethod);
                        } catch (Exception e) {
                            getSetMethods(destClass).put(tmpMethodKey, null);
                            setMethod = getSetMethod(destClass, fieldName, field.getType());
                        }
                    } else {
                        setMethod = getSetMethod(destClass, tmpFieldName, field.getType());
                    }

                } else {
                    setMethod = getSetMethod(destClass, fieldName, field.getType());
                }
                if (setMethod == null) {
                    ignoreFields.append(fieldName).append(",   ");
                    continue;
                }
                descValue = parseValue(value, setMethod.getParameterTypes()[0]);
                if (descValue == null) {
                    continue;
                }
                setMethod.invoke(destObj, descValue);
            } catch (Exception e) {
                ignoreFields.append(fieldName).append(",   ");
            }

        }

        if (ignoreFields.length() > 0) {
            log.debug(ignoreFields.toString());
//            throw new BeanCopyException(ignoreFields.toString());
        }
    }

    private static Map<String, Method> getSetMethods(Class clazz) {
        Map<String, Method> destSetMethodMap = SET_METHOD_MAP.get(clazz);
        if (destSetMethodMap != null) {
            return destSetMethodMap;
        }
        destSetMethodMap = new HashMap<String, Method>(16);
        for (Method method : clazz.getMethods()) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            if (method.getParameterTypes().length != 1) {
                continue;
            }
            destSetMethodMap.put(method.getName(), method);
        }
        SET_METHOD_MAP.put(clazz, destSetMethodMap);
        return destSetMethodMap;
    }

    private static List<Field> getClassFields(Class clazz) {
        List<Field> fields = FIELDS_MAP.get(clazz);
        if (fields != null) {
            return fields;
        }
        fields = new ArrayList<>();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        } while (!Object.class.equals(clazz));
        FIELDS_MAP.put(clazz, fields);
        return fields;
    }

    private static Map<String, Method> getGetMethods(Class clazz) {
        Map<String, Method> destGetMethodMap = GET_METHOD_MAP.get(clazz);
        if (destGetMethodMap != null) {
            return destGetMethodMap;
        }
        destGetMethodMap = new HashMap<String, Method>(16);
        String fieldName = null;
        Method getMethod = null;
        do {
            for (Field field : clazz.getDeclaredFields()) {
                getMethod = null;
                fieldName = field.getName();
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                if (fieldName != null && "MetaDataMap".equalsIgnoreCase(fieldName)) {
                    continue;
                }
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    try {
                        getMethod = clazz.getMethod("get" + fieldName, null);
                    } catch (Exception e) {
                        if ("boolean".equalsIgnoreCase(field.getType().getName())) {
                            if (field.getName().startsWith("is")) {
                                try {
                                    getMethod = clazz.getMethod(field.getName(), null);
                                } catch (Exception e2) {
                                }
                            }
                            if (getMethod == null) {
                                try {
                                    getMethod = clazz.getMethod("is" + fieldName, null);
                                } catch (Exception e2) {
                                }
                            }
                            if (getMethod == null) {
                                try {
                                    getMethod = clazz.getMethod("get" + fieldName, null);
                                } catch (Exception e3) {
                                }
                            }
                        } else {
                            throw e;
                        }
                    }

                    if (getMethod == null) {
                        continue;
                    }
                    destGetMethodMap.put(fieldName, getMethod);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            clazz = clazz.getSuperclass();
        } while (!Object.class.equals(clazz));
        GET_METHOD_MAP.put(clazz, destGetMethodMap);
        return destGetMethodMap;
    }

    private static Method getGetMethod(Class clazz, String fieldName) {
        Map<String, Method> destGetMethodMap = getGetMethods(clazz);
        if (destGetMethodMap == null) {
            return null;
        }
        return destGetMethodMap.get(fieldName);
    }

    private static Method getSetMethod(Class clazz, String fieldName, Type type) {
        Map<String, Method> destSetMethodMap = getSetMethods(clazz);
        if (destSetMethodMap == null) {
            return null;
        }
        String methodName = String.format("set%s", fieldName);
        return destSetMethodMap.get(methodName);
    }


    /**
     * 拷贝除了excludeProperties之外的属性：以descObj为准，从orgObj中获取相同属性名和类型的属性设置到descObj对象中，如果复制属性时出现异常则跳过
     *
     * @param orgObj  源对象
     * @param destObj 目标对象
     * @throws Exception
     */
    public static void copyPropertiesIgnorException(Object orgObj, Object destObj) {
        copyPropertiesIgnorException(orgObj, destObj, null);
    }

    private static Object parseValue(Object value, Class descClass) {
        Object result = null;
        if (value == null) {
            return null;
        }
        switch (value.getClass().getName()) {
            case "int":
                result = parseI32ToObject((int) value, descClass);
                break;
            case "float":
                result = parseFloat2Object((float) value, descClass);
                break;
            case "double":
                result = parseDouble2Object((double) value, descClass);
                break;
            case "java.math.BigDecimal":
                result = parseBigDecimal2Object((BigDecimal) value, descClass);
                break;
            default:
                if (descClass.equals(String.class)) {
                    result = String.valueOf(value);
                } else {
                    result = value;
                }
                break;
        }
        return result;
    }

    private static Object parseI32ToObject(int value, Class descClass) {
        Object result = null;
        switch (descClass.getName()) {
            case "float":
                result = (float) value;
                break;
            case "double":
                result = (double) value;
                break;
            case "long":
                result = (long) value;
                break;
            case "short":
                result = (short) value;
                break;
            case "byte":
                result = (byte) value;
                break;
            case "java.lang.String":
                result = String.valueOf(value);
                break;
            case "java.lang.Float":
                result = new Float(value);
                break;
            case "java.lang.Long":
                result = Long.valueOf(value);
                break;
            case "java.math.BigDecimal":
                result = BigDecimal.valueOf(value);
                break;
            default:
                result = value;
                break;
        }
        return result;
    }

    private static Object parseFloat2Object(float value, Class descClass) {
        Object result = null;
        switch (descClass.getName()) {
            case "int":
                result = (int) value;
                break;
            case "double":
                result = (double) value;
                break;
            case "long":
                result = (long) value;
                break;
            case "short":
                result = (short) value;
                break;
            case "byte":
                result = (byte) value;
                break;
            case "java.lang.String":
                result = String.valueOf(value);
                break;
            case "java.lang.Float":
                result = new Float(value);
                break;
            case "java.lang.Long":
                result = Long.valueOf((long) value);
                break;
            case "java.math.BigDecimal":
                result = BigDecimal.valueOf(value);
                break;
            default:
                result = value;
                break;
        }
        return result;
    }

    private static Object parseDouble2Object(double value, Class descClass) {
        Object result = null;
        switch (descClass.getName()) {
            case "int":
                result = (int) value;
                break;
            case "float":
                result = (float) value;
                break;
            case "long":
                result = (long) value;
                break;
            case "short":
                result = (short) value;
                break;
            case "byte":
                result = (byte) value;
                break;
            case "java.lang.String":
                result = String.valueOf(value);
                break;
            case "java.lang.Float":
                result = new Float(value);
                break;
            case "java.lang.Long":
                result = Long.valueOf((long) value);
                break;
            case "java.math.BigDecimal":
                result = BigDecimal.valueOf(value);
                break;
            default:
                result = value;
                break;
        }
        return result;
    }

    private static Object parseBigDecimal2Object(BigDecimal value, Class descClass) {
        Object result = null;
        switch (descClass.getName()) {
            case "int":
                result = value.intValue();
                break;
            case "float":
                result = value.floatValue();
                break;
            case "double":
                result = value;
                break;
            case "long":
                result = value.longValue();
                break;
            case "short":
                result = value.shortValue();
                break;
            case "byte":
                result = value.byteValue();
                break;
            case "java.lang.String":
                result = String.valueOf(value);
                break;
            case "java.lang.Float":
                result = new Float(value.floatValue());
                break;
            case "java.lang.Long":
                result = Long.valueOf(value.longValue());
                break;
            default:
                result = value;
                break;
        }
        return result;
    }

}
