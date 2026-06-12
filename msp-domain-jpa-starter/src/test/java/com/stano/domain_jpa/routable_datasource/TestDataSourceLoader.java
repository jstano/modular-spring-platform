package com.stano.domain_jpa.routable_datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;

public class TestDataSourceLoader {
  public static RoutableDataSources<Long, DataSource> createDataSources(long... ids) {
    Map<Long, DataSource> map = new LinkedHashMap<>();

    for (long id : ids) {
      map.put(id, createDataSource("jdbc:h2:mem:database" + id, "user" + id, "password" + id));
    }

    return new RoutableDataSources<>(map);
  }

  public static DataSource createDataSource(String jdbcUrl, String username, String password) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }

  private TestDataSourceLoader() {}
}
