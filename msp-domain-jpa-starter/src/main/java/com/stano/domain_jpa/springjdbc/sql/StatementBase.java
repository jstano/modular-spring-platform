package com.stano.domain_jpa.springjdbc.sql;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

abstract class StatementBase {
  final Sql sql;
  final NamedParameterJdbcTemplate jdbcTemplate;
  final String sqlString;
  final Parameters parameters;

  protected StatementBase(Sql sql, NamedParameterJdbcTemplate jdbcTemplate, String sqlString) {
    this.sql = sql;
    this.jdbcTemplate = jdbcTemplate;
    this.sqlString = sqlString;
    this.parameters = new Parameters(sql);
  }

  public Parameters getParameters() {
    return parameters;
  }
}
