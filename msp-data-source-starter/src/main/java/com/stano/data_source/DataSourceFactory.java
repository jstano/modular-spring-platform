package com.stano.data_source;

import com.stano.exceptions.RuntimeSQLException;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

/**
 * Static factory for building {@link DataSource} instances used by the platform's multi-tenant
 * datasource routing.
 */
public final class DataSourceFactory {
  /**
   * Creates a pooled {@link DataSource} backed by HikariCP, configured with the given JDBC
   * connection details and further customized from any {@code spring.datasource.hikari} properties
   * bound from the given {@link Environment}.
   *
   * @param environment the Spring environment to bind additional Hikari properties from
   * @param jdbcUrl the JDBC URL to connect to
   * @param username the database username
   * @param password the database password
   * @return a configured Hikari-backed data source
   */
  public static DataSource createDataSource(
      Environment environment, String jdbcUrl, String username, String password) {
    var dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMinimumIdle(3);
    dataSource.setMaximumPoolSize(100);
    dataSource.setKeepaliveTime(0L);

    Binder.get(environment).bind("spring.datasource.hikari", Bindable.ofInstance(dataSource));

    return dataSource;
  }

  /**
   * Opens a single JDBC connection and wraps it in a {@link ConnectionDataSource}.
   *
   * @param jdbcUrl the JDBC URL to connect to
   * @param username the database username
   * @param password the database password
   * @return a data source wrapping the newly opened connection
   * @throws RuntimeSQLException if the connection cannot be established
   */
  public static ConnectionDataSource createConnectionDataSource(
      String jdbcUrl, String username, String password) {
    try {
      return new ConnectionDataSource(DriverManager.getConnection(jdbcUrl, username, password));
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }

  private DataSourceFactory() {}
}
