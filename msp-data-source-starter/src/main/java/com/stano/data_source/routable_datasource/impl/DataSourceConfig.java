package com.stano.data_source.routable_datasource.impl;

/**
 * Connection configuration for a single routable tenant database, as loaded from the {@code
 * data_source} table by {@link DefaultRoutableDataSourceLoader}.
 *
 * @param id the unique identifier used as the routing key
 * @param name the human-readable name of the database
 * @param jdbcUrl the JDBC URL used to connect to the database
 * @param username the database username
 * @param password the database password
 */
public record DataSourceConfig(
    long id, String name, String jdbcUrl, String username, String password) {}
