package com.stano.data_source.routable_datasource.impl;

import com.stano.data_source.DataSourceFactory;
import com.stano.data_source.routable_datasource.RoutableDataSources;
import com.stano.data_source.routable_datasource.RoutableDataSourcesLoader;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Default {@link RoutableDataSourcesLoader} implementation that reads tenant database connection
 * configuration from a {@code data_source} table, keyed by {@code long} identifiers, and builds a
 * pooled {@link DataSource} for each row.
 */
public class DefaultRoutableDataSourceLoader implements RoutableDataSourcesLoader<Long> {
  private final Environment environment;
  private final JdbcTemplate jdbcTemplate;

  /**
   * Creates a loader that queries the given JDBC template for data source configuration.
   *
   * @param environment the Spring environment used to configure created data sources
   * @param jdbcTemplate the template used to query the {@code data_source} table
   */
  public DefaultRoutableDataSourceLoader(Environment environment, JdbcTemplate jdbcTemplate) {
    this.environment = environment;
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Queries the {@code data_source} table and builds a pooled {@link DataSource} for each
   * configured row, keyed by its identifier.
   *
   * @return the loaded data sources, keyed by database identifier
   */
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
