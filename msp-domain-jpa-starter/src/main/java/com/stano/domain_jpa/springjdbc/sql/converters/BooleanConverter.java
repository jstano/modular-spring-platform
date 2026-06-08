package com.stano.domain_jpa.springjdbc.sql.converters;

import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanConverter {
  private static final String[] TRUE_VALUES = {"y", "yes", "true", "1"};

  public Boolean convertBooleanValue(ResultSet rs, int index, Class<?> type) throws SQLException {
    Object enumStringValue = JdbcUtils.getResultSetValue(rs, index);

    if (Boolean.class.isAssignableFrom(type)) {
      if (enumStringValue != null) {
        return convertStringToBoolean(enumStringValue.toString());
      }

      return null;
    }

    if (enumStringValue != null) {
      return convertStringToBoolean(enumStringValue.toString());
    }

    return Boolean.FALSE;
  }

  private boolean convertStringToBoolean(String enumStringValue) {
    for (String trueValue : TRUE_VALUES) {
      if (enumStringValue.equalsIgnoreCase(trueValue)) {
        return true;
      }
    }

    return false;
  }
}
