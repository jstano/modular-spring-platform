package com.stano.domain_jpa.springjdbc.sql;

import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Locale;

public class QuerySingleResult<T> extends StatementBase {
  private final SingleColumnRowMapper<T> rowMapper;

  public T getSingleResult() {
    return jdbcTemplate.queryForObject(sqlString, parameters.getParameterMap(), rowMapper);
  }

  public QuerySingleResult<T> setParameter(String name, String value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, int value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, Integer value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, long value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, Long value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, BigDecimal value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, LocalDate value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, LocalDateTime value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, LocalTime value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, boolean value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, Collection<?> value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, Locale value) {
    parameters.setParameter(name, value);
    return this;
  }

  public QuerySingleResult<T> setParameter(String name, byte[] value) {
    parameters.setParameter(name, value);
    return this;
  }

  QuerySingleResult(Sql sql, NamedParameterJdbcTemplate jdbcTemplate, String sqlString, Class<T> requiredType) {
    super(sql, jdbcTemplate, sqlString);

    this.rowMapper = new SingleColumnRowMapper<>(requiredType);
  }
}
