package com.stano.domain_jpa.metrics;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMetricsService {
  private final DataSource dataSource;

  public DatabaseMetricsService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public ConnectionPoolMetrics getConnectionPoolMetrics() {
    if (this.dataSource instanceof HikariDataSource) {
      HikariDataSource hikariDataSource = (HikariDataSource) this.dataSource;
      var poolMx = hikariDataSource.getHikariPoolMXBean();
      int activeConnections = poolMx.getActiveConnections();
      int poolSize = poolMx.getTotalConnections();
      int maxPoolSize = hikariDataSource.getMaximumPoolSize();
      int awaitingConnection = poolMx.getThreadsAwaitingConnection();
      long keepaliveTime = hikariDataSource.getKeepaliveTime();
      long connectionTimeout = hikariDataSource.getConnectionTimeout();
      long maxLifeTime = hikariDataSource.getMaxLifetime();

      return new ConnectionPoolMetrics(
          activeConnections,
          poolSize,
          maxPoolSize,
          keepaliveTime,
          connectionTimeout,
          maxLifeTime,
          awaitingConnection);
    }

    return new ConnectionPoolMetrics(0, 0, 0, 0L, 0L, 0L, 0);
  }
}
