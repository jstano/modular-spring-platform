package com.stano.domain_jpa_test;

import com.stano.domain_jpa.springdata.DefaultJpaPersistenceAdapterSpringConfig;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;

public abstract class TestJpaPersistenceAdapterSpringConfig extends DefaultJpaPersistenceAdapterSpringConfig {
  protected DataSource createDataSource(Environment environment) {
    try {
      EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
      return embeddedPostgres.getDatabase("postgres", "test");
    }
    catch (IOException x) {
      throw new RuntimeException(x);
    }
  }
}
