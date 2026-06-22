package com.stano.schema;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

class SchemaManagerTest {
  private static class TestSchemaContext extends DefaultSchemaContext {
    TestSchemaContext() {
      super(
          TestSchemaContext.class.getClassLoader().getResource("db/schema.xml"),
          "db/migration/test");
    }
  }

  private DataSource newPostgresDataSource() throws Exception {
    EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.builder().start();
    try (var connection = embeddedPostgres.getPostgresDatabase().getConnection();
        var statement = connection.createStatement()) {
      statement.execute("create database test");
    }
    return embeddedPostgres.getDatabase("postgres", "test");
  }

  @Test
  void installsSchemaWhenNotYetPresent() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    try (var connection = dataSource.getConnection()) {
      assertThat(schemaContext.schemaIsInstalled(connection)).isFalse();
    }

    SchemaManager.installOrMigrate(dataSource, schemaContext);

    try (var connection = dataSource.getConnection()) {
      assertThat(schemaContext.schemaIsInstalled(connection)).isTrue();
    }
  }

  @Test
  void doesNotMigrateAfterInstall() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    SchemaManager.installOrMigrate(dataSource, schemaContext);

    // migrations should NOT have been applied — install and migrate are mutually exclusive
    assertThat(new FlywaySchemaInstaller().getPendingMigrations(dataSource, schemaContext))
        .isNotEmpty();
  }

  @Test
  void migratesWhenSchemaAlreadyInstalled() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);

    SchemaManager.installOrMigrate(dataSource, schemaContext);

    assertThat(new FlywaySchemaInstaller().getPendingMigrations(dataSource, schemaContext))
        .isEmpty();
  }
}
