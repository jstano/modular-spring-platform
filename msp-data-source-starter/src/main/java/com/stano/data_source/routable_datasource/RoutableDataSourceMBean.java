package com.stano.data_source.routable_datasource;

/**
 * JMX management interface for a {@link RoutableDataSource}, exposing operations to inspect and
 * refresh its set of routed target data sources at runtime.
 */
public interface RoutableDataSourceMBean {
  /**
   * Returns the number of currently registered target data sources.
   *
   * @return the number of target data sources
   */
  int getNumberOfDataSources();

  /** Forces a reload of the target data sources from their backing configuration. */
  void refresh();
}
