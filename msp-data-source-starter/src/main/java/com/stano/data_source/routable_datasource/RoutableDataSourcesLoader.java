package com.stano.data_source.routable_datasource;

import javax.sql.DataSource;

/**
 * Loads the set of tenant {@link DataSource}s that a {@link RoutableDataSource} routes between,
 * keyed by tenant/database identifier.
 *
 * <p>Implementations are called both at startup and, via {@link RoutableDataSource#refresh()} or
 * automatically when an unrecognized key is looked up, to pick up newly added, changed, or removed
 * tenant databases without requiring an application restart.
 *
 * @param <K> the type of the routing key (typically the tenant/database identifier)
 */
public interface RoutableDataSourcesLoader<K> {
  /**
   * Loads the current set of routable data sources.
   *
   * @return the loaded data sources, keyed by routing key
   */
  RoutableDataSources<K, DataSource> loadDataSources();
}
