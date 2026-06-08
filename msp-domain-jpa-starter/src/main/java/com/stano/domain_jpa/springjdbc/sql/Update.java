package com.stano.domain_jpa.springjdbc.sql;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Locale;

public class Update extends StatementBase {
  public int executeUpdate() {
    return jdbcTemplate.update(sqlString, parameters.getParameterMap());
  }

  public int executeUpdate(KeyHolder generatedKeyHolder) {
    return jdbcTemplate.update(sqlString, new MapSqlParameterSource(parameters.getParameterMap()), generatedKeyHolder);
  }

  public int executeUpdate(SqlParameterSource parameterSource) {
    return jdbcTemplate.update(sqlString, parameterSource);
  }

  public int executeUpdate(SqlParameterSource parameterSource, KeyHolder generatedKeyHolder) {
    return jdbcTemplate.update(sqlString, parameterSource, generatedKeyHolder);
  }

  public Update setParameter(String name, String value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, int value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, Integer value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, long value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, Long value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, BigDecimal value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, LocalDate value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, LocalDateTime value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, LocalTime value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, boolean value) {
    parameters.setParameter(name, value);

    return this;
  }

  public <T> Update setParameter(String name, Collection<T> value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, Locale value) {
    parameters.setParameter(name, value);

    return this;
  }

  public Update setParameter(String name, byte[] value) {
    parameters.setParameter(name, value);

    return this;
  }

  Update(Sql sql, NamedParameterJdbcTemplate jdbcTemplate, String sqlString) {
    super(sql, jdbcTemplate, sqlString);
  }
}
