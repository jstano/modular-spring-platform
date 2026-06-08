package com.stano.domain_jpa.springjdbc.sql;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Query<T> extends StatementBase {
  private final ResultSetExtractor<List<T>> resultSetExtractor;

  public List<T> getResultList() {
    return jdbcTemplate.query(sqlString, parameters.getParameterMap(), resultSetExtractor);
  }

  public Optional<T> getSingleResult() {
    List<T> results = jdbcTemplate.query(sqlString, parameters.getParameterMap(), resultSetExtractor);

    if (results.size() == 1) {
      return Optional.of(results.get(0));
    }

    return Optional.empty();
  }

  public Query<T> setParameter(String name, String value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, int value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, Integer value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, long value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, Long value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, BigDecimal value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, LocalDate value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, LocalDateTime value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, LocalTime value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, boolean value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, Collection<?> value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, Locale value) {
    parameters.setParameter(name, value);
    return this;
  }

  public Query<T> setParameter(String name, byte[] value) {
    parameters.setParameter(name, value);
    return this;
  }

  Query(Sql sql, NamedParameterJdbcTemplate jdbcTemplate, String sqlString, ResultSetExtractor<List<T>> resultSetExtractor) {
    super(sql, jdbcTemplate, sqlString);

    this.resultSetExtractor = resultSetExtractor;
  }

  Query(Sql sql, NamedParameterJdbcTemplate jdbcTemplate, String sqlString, RowMapper<T> rowMapper) {
    super(sql, jdbcTemplate, sqlString);

    this.resultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
  }
}
