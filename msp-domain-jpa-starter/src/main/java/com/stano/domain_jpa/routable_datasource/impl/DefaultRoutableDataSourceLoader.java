package com.stano.domain_jpa.routable_datasource.impl;

import com.stano.domain_jpa.routable_datasource.RoutableDataSources;
import com.stano.domain_jpa.routable_datasource.RoutableDataSourcesLoader;
import com.stano.domain_jpa.springjdbc.hikari.HikariDataSourceFactory;
import com.stano.domain_jpa.springjdbc.sql.Sql;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.stream.Collectors;

public class DefaultRoutableDataSourceLoader implements RoutableDataSourcesLoader<Long> {
  private final Environment environment;
  private final Sql sql;

  public DefaultRoutableDataSourceLoader(Environment environment,
                                         Sql sql) {
    this.environment = environment;
    this.sql = sql;
  }

  @Override
  public RoutableDataSources<Long, DataSource> loadDataSources() {
    return new RoutableDataSources<Long, DataSource>(sql.query("select * from data_source", DataSourceConfig.class)
                                      .getResultList()
                                      .stream()
                                      .collect(Collectors.toMap(DataSourceConfig::id,
                                                                it -> HikariDataSourceFactory.createDataSource(environment,
                                                                                                               it.jdbcUrl(),
                                                                                                               it.username(),
                                                                                                               it.password()))));
  }
}
