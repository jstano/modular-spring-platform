package com.stano.domain_jpa.datasource;

import com.stano.exceptions.RuntimeSQLException;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

public final class DataSourceFactory {
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
