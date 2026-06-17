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

public class RoutableDataSource extends AbstractRoutingDataSource
    implements RoutableDataSourceMBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(RoutableDataSource.class);

  private final RoutableDataSourcesLoader<Long> routableDataSourcesLoader;
  private final RoutableDataSources<Long, DataSource> routableDataSources;
  private final Map<Object, Object> targetDataSourcesMap = new LinkedHashMap<>();

  public RoutableDataSource(RoutableDataSourcesLoader<Long> routableDataSourcesLoader) {
    this.routableDataSourcesLoader = routableDataSourcesLoader;
    this.routableDataSources = routableDataSourcesLoader.loadDataSources();

    routableDataSources.forEach(targetDataSourcesMap::put);
    setTargetDataSources(targetDataSourcesMap);
    afterPropertiesSet();
  }

  @Override
  protected Long determineCurrentLookupKey() {
    Long databaseId = DatabaseContextHolder.getDatabaseId();

    if (databaseId != null) {
      return databaseId;
    }

    return routableDataSources.keySet().iterator().next();
  }

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

  @Override
  public int getNumberOfDataSources() {
    return routableDataSources.size();
  }

  @Override
  public void refresh() {
    synchronized (this) {
      mergeDataSources(routableDataSourcesLoader.loadDataSources());
    }
  }

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
