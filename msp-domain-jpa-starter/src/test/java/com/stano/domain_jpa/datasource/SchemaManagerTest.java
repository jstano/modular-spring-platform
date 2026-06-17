package com.stano.domain_jpa.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

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

  private ApplicationContext applicationContextFor(
      SchemaContext schemaContext, boolean autoMigrate) {
    return applicationContextFor(schemaContext, false, autoMigrate, false);
  }

  private ApplicationContext applicationContextFor(
      SchemaContext schemaContext, boolean autoMigrate, boolean skipMigrationCheck) {
    return applicationContextFor(schemaContext, false, autoMigrate, skipMigrationCheck);
  }

  private ApplicationContext applicationContextFor(
      SchemaContext schemaContext,
      boolean autoInstall,
      boolean autoMigrate,
      boolean skipMigrationCheck) {
    var applicationContext = mock(ApplicationContext.class);
    var environment = mock(Environment.class);
    when(applicationContext.getBeanProvider(SchemaContext.class))
        .thenReturn(
            new ObjectProvider<SchemaContext>() {
              @Override
              public SchemaContext getObject() {
                return schemaContext;
              }

              @Override
              public SchemaContext getObject(Object... args) {
                return schemaContext;
              }

              @Override
              public SchemaContext getIfAvailable() {
                return schemaContext;
              }
            });
    when(applicationContext.getEnvironment()).thenReturn(environment);
    when(environment.getProperty("msp.schema.auto-install", Boolean.class, false))
        .thenReturn(autoInstall);
    when(environment.getProperty("msp.schema.auto-migrate", Boolean.class, false))
        .thenReturn(autoMigrate);
    when(environment.getProperty("msp.schema.skip-migration-check", Boolean.class, false))
        .thenReturn(skipMigrationCheck);
    return applicationContext;
  }

  @Test
  void schemaIsInstalledWhenNotYetPresent() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    SchemaManager.migrate(applicationContextFor(schemaContext, true, false, false), dataSource);

    try (var connection = dataSource.getConnection()) {
      assertThat(schemaContext.schemaIsInstalled(connection)).isTrue();
    }
  }

  @Test
  void noExceptionWhenAutoMigrateFalseAndNoPendingMigrations() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);
    new FlywaySchemaInstaller().migrateSchema(dataSource, schemaContext);

    SchemaManager.migrate(applicationContextFor(schemaContext, false), dataSource);
  }

  @Test
  void throwsWhenAutoMigrateFalseAndPendingMigrationsExist() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);

    assertThatThrownBy(
            () -> SchemaManager.migrate(applicationContextFor(schemaContext, false), dataSource))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("pending migrations");
  }

  @Test
  void migratesWhenAutoMigrateTrueAndPendingMigrationsExist() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);

    SchemaManager.migrate(applicationContextFor(schemaContext, true), dataSource);

    assertThat(new FlywaySchemaInstaller().getPendingMigrations(dataSource, schemaContext))
        .isEmpty();
  }

  @Test
  void noExceptionWhenSkipMigrationCheckTrueAndPendingMigrationsExist() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);

    SchemaManager.migrate(applicationContextFor(schemaContext, false, false, true), dataSource);

    assertThat(new FlywaySchemaInstaller().getPendingMigrations(dataSource, schemaContext))
        .isNotEmpty();
  }

  @Test
  void throwsWhenAutoInstallFalseAndSchemaNotInstalled() throws Exception {
    var dataSource = newPostgresDataSource();
    var schemaContext = new TestSchemaContext();

    assertThatThrownBy(
            () ->
                SchemaManager.migrate(
                    applicationContextFor(schemaContext, false, false, false), dataSource))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("not installed")
        .hasMessageContaining("auto-install");
  }
}
