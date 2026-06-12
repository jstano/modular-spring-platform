package com.stano.domain_jpa.routable_datasource.impl;

public record DataSourceConfig(
    long id, String name, String jdbcUrl, String username, String password) {}
