package com.stano.domain_jpa.springjdbc.sql;

import com.stano.domain_jpa.springjdbc.sql.converters.BooleanConverter;
import com.stano.domain_jpa.springjdbc.sql.converters.EnumConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorRowMapper<T> implements RowMapper<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConstructorRowMapper.class);

  private final Constructor<T> mappedConstructor;
  private final List<Class<?>> mappedParameters;
  private final BooleanConverter booleanConverter;
  private final EnumConverter enumConverter;

  public static <T> ConstructorRowMapper<T> newInstance(Class<T> mappedClass) {
    return new ConstructorRowMapper<>(mappedClass);
  }

  @Override
  public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
    ResultSetMetaData resultSetMetaData = rs.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();

    List<Object> values = new ArrayList<>();

    for (int index = 1; index <= columnCount; index++) {
      String column = JdbcUtils.lookupColumnName(resultSetMetaData, index);
      Class<?> type = mappedParameters.get(index - 1);

      if (type != null) {
        Object value = getColumnValue(rs, index, type);

        if (LOGGER.isDebugEnabled() && rowNumber == 0) {
          LOGGER.debug("Mapping column '" + column + "' to property '" +
                       type.getName() + "' of type " + type);
        }

        values.add(value);
      }
    }

    return BeanUtils.instantiateClass(mappedConstructor, values.toArray());
  }

  private Object getColumnValue(ResultSet rs, int index, Class<?> type) throws SQLException {
    if (Enum.class.isAssignableFrom(type)) {
      return enumConverter.convertEnumValue(rs, index, type);
    }

    if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
      return booleanConverter.convertBooleanValue(rs, index, type);
    }

    return JdbcUtils.getResultSetValue(rs, index, type);
  }

  @SuppressWarnings("unchecked")
  private ConstructorRowMapper(Class<T> mappedClass) {
    mappedConstructor = (Constructor<T>)mappedClass.getDeclaredConstructors()[0];
    mappedParameters = Arrays.stream(mappedConstructor.getParameters()).map(Parameter::getType).collect(Collectors.toList());
    booleanConverter = new BooleanConverter();
    enumConverter = new EnumConverter(mappedConstructor);
  }
}
