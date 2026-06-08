package com.stano.domain_jpa.springjdbc.sql;

import com.stano.domain_jpa.springjdbc.datasource.ConnectionDataSource;
import com.stano.domain_jpa.springjdbc.datasource.DelegatingConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class Sql {
  public enum BooleanMode {NATIVE, YN, YES_NO}

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final BooleanMode booleanMode;

  public Sql(DataSource dataSource) {
    this(dataSource, BooleanMode.NATIVE);
  }

  public Sql(JdbcTemplate jdbcTemplate) {
    this(jdbcTemplate, BooleanMode.NATIVE);
  }

  public Sql(Connection connection) {
    this(connection, BooleanMode.NATIVE);
  }

  public Sql(DataSource dataSource, BooleanMode booleanMode) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.booleanMode = booleanMode;
  }

  public Sql(JdbcTemplate jdbcTemplate, BooleanMode booleanMode) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    this.booleanMode = booleanMode;
  }

  public Sql(Connection connection, BooleanMode booleanMode) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(new ConnectionDataSource(new DelegatingConnection(connection)));
    this.booleanMode = booleanMode;
  }

  public BooleanMode getBooleanMode() {
    return booleanMode;
  }

  public <T> Query<T> query(String sqlString, Class<T> resultClass) {
    return new Query<>(this, jdbcTemplate, sqlString, new CustomBeanPropertyRowMapper<>(resultClass));
  }

  public <T> Query<T> query(String sqlString, ResultSetExtractor<List<T>> resultSetExtractor) {
    return new Query<>(this, jdbcTemplate, sqlString, resultSetExtractor);
  }

  public <T> Query<T> query(String sqlString, RowMapper<T> rowMapper) {
    return new Query<>(this, jdbcTemplate, sqlString, rowMapper);
  }

  public <T> QuerySingleResult<T> queryForSingleResult(String sqlString, Class<T> requiredType) {
    return new QuerySingleResult<>(this, jdbcTemplate, sqlString, requiredType);
  }

  public Update update(String sqlString) {
    return new Update(this, jdbcTemplate, sqlString);
  }

  public NamedParameterJdbcTemplate jdbcTemplate() {
    return jdbcTemplate;
  }

  public int[] batchUpdate(String sql, List<?> items) {
    return jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(items.toArray()));
  }
}
