package com.stano.domain_jpa;

import com.stano.domain_jpa.datasource.JpaDataSourceAutoConfiguration;
import com.stano.domain_jpa.datasource.SchemaManager;
import com.stano.exceptions.RuntimeSQLException;
import com.stano.schema.MigrationAutoConfiguration;
import com.stano.schema.installer.schemacontext.SchemaContext;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration providing an embedded PostgreSQL {@link DataSource} for JPA slice tests, so
 * tests run against a real Postgres engine instead of an in-memory substitute.
 *
 * <p>Runs before {@code JpaDataSourceAutoConfiguration} and {@link MigrationAutoConfiguration} so
 * the embedded datasource is available before the platform's own datasource/schema
 * auto-configuration runs.
 */
@AutoConfiguration(
    before = {JpaDataSourceAutoConfiguration.class, MigrationAutoConfiguration.class})
public class PostgresJpaTestConfig {
  /**
   * Starts an embedded PostgreSQL instance, creates a {@code test} database on it, and installs the
   * application's schema (via {@link SchemaContext}, if a bean of that type is available) before
   * returning the resulting datasource.
   *
   * <p>Only active if the application has not defined its own {@code DataSource} bean.
   *
   * @param applicationContext used to look up an optional {@link SchemaContext} bean
   * @return a datasource connected to the freshly created, schema-installed test database
   * @throws RuntimeSQLException if the embedded instance or database creation fails
   */
  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(ApplicationContext applicationContext) {
    try {
      EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
      try (var conn = embeddedPostgres.getPostgresDatabase().getConnection();
          var stmt = conn.createStatement()) {
        stmt.execute("create database test");
      }
      DataSource dataSource = embeddedPostgres.getDatabase("postgres", "test");
      var schemaContext = applicationContext.getBeanProvider(SchemaContext.class).getIfAvailable();
      if (schemaContext != null) {
        SchemaManager.installSchema(dataSource, schemaContext);
      }
      return dataSource;
    } catch (IOException | SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }
}
