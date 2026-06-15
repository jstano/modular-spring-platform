package com.stano.domain_jpa;

import com.stano.domain_jpa.schema.SchemaManager;
import com.stano.exceptions.RuntimeSQLException;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class PostgresJpaTestConfig {
  @Bean
  public DataSource dataSource(ApplicationContext applicationContext) {
    try {
      EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
      try (var conn = embeddedPostgres.getPostgresDatabase().getConnection();
          var stmt = conn.createStatement()) {
        stmt.execute("create database test");
      }
      DataSource dataSource = embeddedPostgres.getDatabase("postgres", "test");
      SchemaManager.migrate(applicationContext, dataSource);
      return dataSource;
    } catch (IOException | SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }
}
