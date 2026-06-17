package com.stano.data_source.routable_datasource;

public interface RoutableDataSourceMBean {
  int getNumberOfDataSources();

  void refresh();
}
