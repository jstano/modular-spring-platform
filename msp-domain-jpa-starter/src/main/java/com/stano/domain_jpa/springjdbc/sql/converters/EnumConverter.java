package com.stano.domain_jpa.springjdbc.sql.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EnumConverter {
  private final Map<Class<?>, Method> mappedEnumConverterMethods;

  public EnumConverter(Class<?> mappedClass) {
    this.mappedEnumConverterMethods = getMappedEnumConverterMethods(mappedClass);
  }

  public EnumConverter(Constructor<?> mappedConstructor) {
    this.mappedEnumConverterMethods = getMappedEnumConverterMethods(mappedConstructor);
  }

  public Enum<?> convertEnumValue(ResultSet rs, int index, Class<?> enumType) throws SQLException {
    String enumStringValue = (String)JdbcUtils.getResultSetValue(rs, index, String.class);

    if (enumStringValue != null) {
      Method fromCodeMethod = mappedEnumConverterMethods.get(enumType);

      if (fromCodeMethod != null) {
        try {
          return (Enum<?>)fromCodeMethod.invoke(null, enumStringValue);
        }
        catch (IllegalAccessException | InvocationTargetException x) {
          throw new IllegalStateException(x);
        }
      }
    }

    return null;
  }

  private Map<Class<?>, Method> getMappedEnumConverterMethods(Class<?> mappedClass) {
    Map<Class<?>, Method> mappedEnumConverterMethods = new HashMap<>();

    PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(mappedClass);

    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      Method writeMethod = propertyDescriptor.getWriteMethod();

      if (writeMethod != null && writeMethod.getParameterCount() == 1) {
        Class<?> parameterType = writeMethod.getParameterTypes()[0];

        if (Enum.class.isAssignableFrom(parameterType) && !mappedEnumConverterMethods.containsKey(parameterType)) {
          mappedEnumConverterMethods.put(parameterType, getFromCodeMethod(parameterType));
        }
      }
    }

    return mappedEnumConverterMethods;
  }

  private Map<Class<?>, Method> getMappedEnumConverterMethods(Constructor<?> mappedConstructor) {
    Map<Class<?>, Method> mappedEnumConverterMethods = new HashMap<>();

    for (Parameter parameter : mappedConstructor.getParameters()) {
      Class<?> parameterType = parameter.getType();

      if (Enum.class.isAssignableFrom(parameterType) && !mappedEnumConverterMethods.containsKey(parameterType)) {
        mappedEnumConverterMethods.put(parameterType, getFromCodeMethod(parameterType));
      }
    }

    return mappedEnumConverterMethods;
  }

  private Method getFromCodeMethod(Class<?> enumType) {
    try {
      return enumType.getMethod("fromCode", String.class);
    }
    catch (NoSuchMethodException ignored) {
      try {
        return enumType.getMethod("valueOf", String.class);
      }
      catch (NoSuchMethodException ignored2) {
        return null;
      }
    }
  }
}
