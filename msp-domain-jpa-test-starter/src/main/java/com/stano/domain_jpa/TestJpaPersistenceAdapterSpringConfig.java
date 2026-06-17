package com.stano.domain_jpa;

import com.stano.domain_jpa.datasource.SchemaManager;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(DefaultJpaSpringConfig.class)
public abstract class TestJpaPersistenceAdapterSpringConfig {
  @Bean
  public DataSource dataSource(ApplicationContext applicationContext) {
    try {
      EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
      try (var conn = embeddedPostgres.getPostgresDatabase().getConnection();
          var stmt = conn.createStatement()) {
        stmt.execute("CREATE DATABASE test");
      }
      DataSource dataSource = embeddedPostgres.getDatabase("postgres", "test");
      SchemaManager.migrate(applicationContext, dataSource);
      return dataSource;
    } catch (IOException | SQLException x) {
      throw new RuntimeException(x);
    }
  }
}
