package com.stano.data_source.routable_datasource;

import javax.sql.DataSource;

public interface RoutableDataSourcesLoader<K> {
  RoutableDataSources<K, DataSource> loadDataSources();
}
