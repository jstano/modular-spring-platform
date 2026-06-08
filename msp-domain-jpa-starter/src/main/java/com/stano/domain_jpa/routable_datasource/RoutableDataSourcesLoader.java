package com.stano.domain_jpa.routable_datasource;

import javax.sql.DataSource;

public interface RoutableDataSourcesLoader<K> {
  RoutableDataSources<K, DataSource> loadDataSources();
}
