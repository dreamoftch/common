package com.tch.test.common.utils;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.tch.test.common.date.DateUtils;

public class ObjectUtil {

  private static Logger logger = Logger.getLogger(ObjectUtil.class);

  @SuppressWarnings("IllegalCatch")
  public static <T> void setValueIfIsNull(T t, String fieldName, Object fieldValue) {
    try {
      Method getterMethod = t.getClass().getDeclaredMethod(getterMethodName(fieldName));
      Object value = getterMethod.invoke(t);
      if (value != null) {
        return;
      }
      Method setterMethod = t.getClass().getDeclaredMethod(setterMethodName(fieldName),
          getterMethod.getReturnType());
      setterMethod.invoke(t, fieldValue);
    } catch (Exception e) {
      logger.error("Error occured when setValueIfIsNull: ", e);
    }
  }

  @SuppressWarnings("IllegalCatch")
  public static <T> Method setterMethod(Class<?> clazz, String fieldName) {
    try {
      Method getterMethod = clazz.getDeclaredMethod(getterMethodName(fieldName));
      if (getterMethod == null) {
        return null;
      }
      return clazz.getDeclaredMethod(setterMethodName(fieldName),
          getterMethod.getReturnType());
    } catch (NoSuchMethodException e) {
      logger.error("Error occured when setterMethod: ", e);
      return null;
    }
  }

  public static String setterMethodName(String fieldName) {
    if (fieldName == null || fieldName.isEmpty()) {
      return null;
    }
    return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
  }

  public static String getterMethodName(String fieldName) {
    if (fieldName == null || fieldName.isEmpty()) {
      return null;
    }
    return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
  }

  @SuppressWarnings("IllegalCatch")
  public static <T> Object getFieldValue(T t, String fieldName) {
    try {
      Method getterMethod = t.getClass().getDeclaredMethod(getterMethodName(fieldName));
      return getterMethod.invoke(t);
    } catch (Exception e) {
      logger.error("Error occured when getFieldValue: ", e);
    }
    return null;
  }

  @SuppressWarnings("IllegalCatch")
  public static <T> Object setFieldValue(T t, String fieldName, Object fieldValue) {
    try {
      Method setterMethod = t.getClass().getDeclaredMethod(setterMethodName(fieldName),
          fieldValue.getClass());
      return setterMethod.invoke(t, fieldValue);
    } catch (Exception e) {
      logger.error("Error occured when setFieldValue: ", e);
    }
    return null;
  }

  @SuppressWarnings({"unchecked", "IllegalCatch", "JavadocMethod"})
  public static <T> void mergeObject(T t, Map<String, Object> params) {
    try {
      if (params == null || params.isEmpty()) {
        return;
      }
      Class<?> clazz = t.getClass();
      params.forEach((key, value) -> {
        try {
          if (!iskeyValid(clazz, key)) {
            return;
          }
          Method getterMethod = clazz.getDeclaredMethod(getterMethodName(key));
          if (getterMethod == null) {
            return;
          }
          Class<?> type = getterMethod.getReturnType();
          Method setterMethod = clazz.getDeclaredMethod(setterMethodName(key), type);
          if (value == null) {
            setterMethod.invoke(t, (Object) null);
            return;
          }
          if (String.class.isAssignableFrom(type)) {
            if (value instanceof Map) {
              //if the value is a map(json object), store the json string
              setterMethod.invoke(t, JSON.toJSONString((Map<String, Object>) value));
              return;
            }
            setterMethod.invoke(t, (String) value);
          } else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
            setterMethod.invoke(t, Integer.valueOf(String.valueOf(value)));
          } else if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
            setterMethod.invoke(t, Long.valueOf(String.valueOf(value)));
          } else if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
            setterMethod.invoke(t, Double.valueOf(String.valueOf(value)));
          } else if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
            setterMethod.invoke(t, Float.valueOf(String.valueOf(value)));
          } else if (Date.class.isAssignableFrom(type)) {
            setterMethod.invoke(t, DateUtils.parse(String.valueOf(value)));
          }
        } catch (Exception e) {
          logger.error("Error occured in mergeObject params.forEach: ", e);
        }
      });
    } catch (Exception e) {
      logger.error("Error occured when mergeObject: ", e);
    }
  }

  @SuppressWarnings("IllegalCatch")
  private static boolean iskeyValid(Class<?> clazz, String key) {
    try {
      Method getterMethod = clazz.getDeclaredMethod(getterMethodName(key));
      return getterMethod != null;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

}
