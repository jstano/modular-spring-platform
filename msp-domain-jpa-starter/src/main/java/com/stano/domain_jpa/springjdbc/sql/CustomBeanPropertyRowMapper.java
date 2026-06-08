package com.stano.domain_jpa.springjdbc.sql;

import com.stano.domain_jpa.springjdbc.sql.converters.BooleanConverter;
import com.stano.domain_jpa.springjdbc.sql.converters.EnumConverter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
  private final BooleanConverter booleanConverter;
  private final EnumConverter enumConverter;

  public CustomBeanPropertyRowMapper(Class<T> mappedClass) {
    super(mappedClass);

    booleanConverter = new BooleanConverter();
    enumConverter = new EnumConverter(mappedClass);
  }

  @Override
  protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor propertyDescriptor) throws SQLException {
    Class<?> propertyType = propertyDescriptor.getPropertyType();

    if (Enum.class.isAssignableFrom(propertyType)) {
      return enumConverter.convertEnumValue(rs, index, propertyType);
    }

    if (Boolean.class.isAssignableFrom(propertyType) || boolean.class.isAssignableFrom(propertyType)) {
      return booleanConverter.convertBooleanValue(rs, index, propertyType);
    }

    return JdbcUtils.getResultSetValue(rs, index, propertyType);
  }
}
