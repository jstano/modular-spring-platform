package com.stano.domain_jpa.routable_datasource;

public interface RoutableDataSourceMBean {
  int getNumberOfDataSources();

  void refresh();
}
