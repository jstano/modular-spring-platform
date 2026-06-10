package com.stano.domain_jpa.routable_datasource.impl;

import com.stano.domain_jpa.routable_datasource.RoutableDataSources;
import com.stano.domain_jpa.routable_datasource.RoutableDataSourcesLoader;
import com.stano.domain_jpa.datasource.DataSourceFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.stream.Collectors;

public class DefaultRoutableDataSourceLoader implements RoutableDataSourcesLoader<Long> {
  private final Environment environment;
  private final JdbcTemplate jdbcTemplate;

  public DefaultRoutableDataSourceLoader(Environment environment,
                                         JdbcTemplate jdbcTemplate) {
    this.environment = environment;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public RoutableDataSources<Long, DataSource> loadDataSources() {
    return new RoutableDataSources<>(
      jdbcTemplate.query("select * from data_source", new DataClassRowMapper<>(DataSourceConfig.class))
                  .stream()
                  .collect(Collectors.toMap(DataSourceConfig::id,
                                            it -> DataSourceFactory.createDataSource(environment,
                                                                                     it.jdbcUrl(),
                                                                                     it.username(),
                                                                                     it.password()))));
  }
}
