package com.stano.data_source.routable_datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Multi-tenant {@link DataSource} that routes each call to the underlying data source matching the
 * identifier currently held in {@link DatabaseContextHolder}.
 *
 * <p>If no identifier is set for the current thread, calls fall back to an arbitrary configured
 * data source. If the current identifier is not (yet) known, the underlying data sources are
 * reloaded via the configured {@link RoutableDataSourcesLoader} before the lookup is retried, so
 * newly registered tenants become routable without a restart. {@link #refresh()} can also be called
 * explicitly (e.g. from a JMX console, since this class implements {@link RoutableDataSourceMBean})
 * to force a reload.
 */
public class RoutableDataSource extends AbstractRoutingDataSource
    implements RoutableDataSourceMBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(RoutableDataSource.class);

  private final RoutableDataSourcesLoader<Long> routableDataSourcesLoader;
  private final RoutableDataSources<Long, DataSource> routableDataSources;
  private final Map<Object, Object> targetDataSourcesMap = new LinkedHashMap<>();

  /**
   * Creates a routable data source, eagerly loading the initial set of target data sources.
   *
   * @param routableDataSourcesLoader the loader used to (re)load target data sources
   */
  public RoutableDataSource(RoutableDataSourcesLoader<Long> routableDataSourcesLoader) {
    this.routableDataSourcesLoader = routableDataSourcesLoader;
    this.routableDataSources = routableDataSourcesLoader.loadDataSources();

    routableDataSources.forEach(targetDataSourcesMap::put);
    setTargetDataSources(targetDataSourcesMap);
    afterPropertiesSet();
  }

  /**
   * Determines the routing key for the current call, based on the identifier held in {@link
   * DatabaseContextHolder} for the current thread.
   *
   * @return the current database identifier, or an arbitrary known key if none is set for this
   *     thread
   */
  @Override
  protected Long determineCurrentLookupKey() {
    Long databaseId = DatabaseContextHolder.getDatabaseId();

    if (databaseId != null) {
      return databaseId;
    }

    return routableDataSources.keySet().iterator().next();
  }

  /**
   * Resolves the target data source for the current lookup key, reloading the known data sources
   * first if the key is not currently recognized.
   *
   * @return the target data source for the current lookup key
   */
  @Override
  protected DataSource determineTargetDataSource() {
    Long lookupKey = determineCurrentLookupKey();

    synchronized (this) {
      if (!routableDataSources.containsKey(lookupKey)) {
        mergeDataSources(routableDataSourcesLoader.loadDataSources());
      }
    }

    return super.determineTargetDataSource();
  }

  /**
   * Returns the number of currently registered target data sources.
   *
   * @return the number of target data sources
   */
  @Override
  public int getNumberOfDataSources() {
    return routableDataSources.size();
  }

  /**
   * Forces a reload of the target data sources from the configured {@link
   * RoutableDataSourcesLoader}, adding, replacing, or removing entries as needed.
   */
  @Override
  public void refresh() {
    synchronized (this) {
      mergeDataSources(routableDataSourcesLoader.loadDataSources());
    }
  }

  /**
   * Merges a freshly loaded set of data sources into the currently registered ones: new keys are
   * added, changed data sources are replaced (closing the old one), and keys no longer present are
   * removed (closing their data source).
   *
   * @param newRoutableDataSources the freshly loaded data sources to merge in
   */
  private void mergeDataSources(RoutableDataSources<Long, DataSource> newRoutableDataSources) {
    Set<Long> staleKeys = new HashSet<>(routableDataSources.keySet());

    newRoutableDataSources.forEach(
        (key, newDataSource) -> {
          staleKeys.remove(key);

          DataSource currentDataSource = routableDataSources.get(key);

          if (currentDataSource == null) {
            LOGGER.info("Adding DataSource with key {}", key);
            routableDataSources.put(key, newDataSource);
          } else if (dataSourceHasChanged(currentDataSource, (DataSource) newDataSource)) {
            LOGGER.info("Replacing DataSource with key {} due to changes", key);
            routableDataSources.put(key, newDataSource);
            closeDataSource(currentDataSource);
          }
        });

    for (Long staleKey : staleKeys) {
      DataSource dataSource = routableDataSources.get(staleKey);
      LOGGER.info("Removing DataSource with key {}", staleKey);
      routableDataSources.removeInternal(staleKey);
      closeDataSource(dataSource);
    }

    targetDataSourcesMap.clear();
    routableDataSources.forEach(targetDataSourcesMap::put);
    afterPropertiesSet();
  }

  private boolean dataSourceHasChanged(DataSource currentDataSource, DataSource newDataSource) {
    if (!StringUtils.equals(getJdbcUrl(currentDataSource), getJdbcUrl(newDataSource))) {
      return true;
    }

    if (!StringUtils.equals(getUsername(currentDataSource), getUsername(newDataSource))) {
      return true;
    }

    if (!StringUtils.equals(getPassword(currentDataSource), getPassword(newDataSource))) {
      return true;
    }

    return !StringUtils.equals(getDriver(currentDataSource), getDriver(newDataSource));
  }

  private String getJdbcUrl(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      return ((HikariDataSource) dataSource).getJdbcUrl();
    }

    return null;
  }

  private String getUsername(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      return ((HikariDataSource) dataSource).getUsername();
    }

    return null;
  }

  private String getPassword(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      return ((HikariDataSource) dataSource).getPassword();
    }

    return null;
  }

  private String getDriver(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      return ((HikariDataSource) dataSource).getDriverClassName();
    }

    return null;
  }

  private void closeDataSource(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      try {
        ((HikariDataSource) dataSource).close();
      } catch (Exception ignored) {
      }
    }
  }
}
