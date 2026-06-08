package com.stano.domain_jpa.springjdbc.hikari;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

public final class HikariDataSourceFactory {
  public static DataSource createDataSource(Environment environment,
                                            String jdbcUrl,
                                            String username,
                                            String password) {
    var dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMinimumIdle(environment.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 3));
    dataSource.setMaximumPoolSize(environment.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 100));
    dataSource.setKeepaliveTime(environment.getProperty("spring.datasource.hikari.keepalive-time", Long.class, 0L));
    dataSource.setConnectionTimeout(environment.getProperty("spring.datasource.hikari.connection-timeout", Long.class, 30000L));
    dataSource.setMaxLifetime(environment.getProperty("spring.datasource.hikari.max-lifetime", Long.class, 1800000L));

    return dataSource;
  }

  private HikariDataSourceFactory() {
  }
}
