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

@AutoConfiguration(
    before = {JpaDataSourceAutoConfiguration.class, MigrationAutoConfiguration.class})
public class PostgresJpaTestConfig {
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
