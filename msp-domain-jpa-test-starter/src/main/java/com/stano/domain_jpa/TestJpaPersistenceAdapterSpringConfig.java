package com.stano.domain_jpa;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;

public abstract class TestJpaPersistenceAdapterSpringConfig extends DefaultJpaSpringConfig {
  @Override
  protected DataSource createDataSource(ApplicationContext applicationContext) {
    try {
      EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
      return embeddedPostgres.getDatabase("postgres", "test");
    }
    catch (IOException x) {
      throw new RuntimeException(x);
    }
  }
}
