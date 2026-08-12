/**
 * Multi-tenant datasource routing built around a {@link java.lang.ThreadLocal}-held database
 * identifier: {@link com.stano.data_source.routable_datasource.DatabaseContextHolder} stores the
 * current identifier, {@link com.stano.data_source.routable_datasource.RoutableDataSource} routes
 * JDBC/JPA calls to the matching target data source, and {@link
 * com.stano.data_source.routable_datasource.RoutableDataSourcesLoader} supplies the set of target
 * data sources to route between.
 */
package com.stano.data_source.routable_datasource;
