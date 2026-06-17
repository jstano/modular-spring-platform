package com.stano.data_source.routable_datasource.impl;

public record DataSourceConfig(
    long id, String name, String jdbcUrl, String username, String password) {}
