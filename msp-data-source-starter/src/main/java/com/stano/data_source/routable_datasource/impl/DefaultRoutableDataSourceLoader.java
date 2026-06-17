package com.stano.data_source.routable_datasource.impl;

import com.stano.data_source.DataSourceFactory;
import com.stano.data_source.routable_datasource.RoutableDataSources;
import com.stano.data_source.routable_datasource.RoutableDataSourcesLoader;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultRoutableDataSourceLoader implements RoutableDataSourcesLoader<Long> {
  private final Environment environment;
  private final JdbcTemplate jdbcTemplate;

  public DefaultRoutableDataSourceLoader(Environment environment, JdbcTemplate jdbcTemplate) {
    this.environment = environment;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public RoutableDataSources<Long, DataSource> loadDataSources() {
    return new RoutableDataSources<>(
        jdbcTemplate
            .query("select * from data_source", new DataClassRowMapper<>(DataSourceConfig.class))
            .stream()
            .collect(
                Collectors.toMap(
                    DataSourceConfig::id,
                    it ->
                        DataSourceFactory.createDataSource(
                            environment, it.jdbcUrl(), it.username(), it.password()))));
  }
}
