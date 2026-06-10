package com.stano.domain_jpa.schema;

import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.schema.model.Version;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaManager {
  private static final Logger logger = LoggerFactory.getLogger(SchemaManager.class);

  public void installOrMigrate(DataSource dataSource, SchemaContext schemaContext, boolean autoMigrate) {
    try (Connection connection = dataSource.getConnection()) {
      if (!schemaContext.schemaIsInstalled(connection)) {
        installSchema(dataSource, schemaContext);
      } else if (schemaContext.isVersionBased()) {
        migrateIfNeeded(dataSource, schemaContext, connection, autoMigrate);
      }
    } catch (SQLException x) {
      throw new IllegalStateException(x);
    }
  }

  public void migrate(DataSource dataSource, SchemaContext schemaContext) {
    try (Connection connection = dataSource.getConnection()) {
      if (!schemaContext.isVersionBased()) {
        logger.info("Schema is not version-based; skipping migration");
        return;
      }

      Version databaseVersion = schemaContext.getDatabaseVersion(connection);
      if (databaseVersion == null) {
        logger.info("Database version is null; skipping migration");
        return;
      }

      Version schemaVersion = schemaContext.getSchemaVersion();
      if (databaseVersion.compareTo(schemaVersion) >= 0) {
        logger.info("Database version is up to date; skipping migration");
        return;
      }

      var migrationLocator = schemaContext.getMigrationScriptLocator(connection);
      if (migrationLocator == null) {
        logger.warn("Database version {} is behind schema version {}, but no migration scripts found",
                    databaseVersion, schemaVersion);
        return;
      }

      executeMigration(dataSource, migrationLocator.getResourcePath());
    } catch (SQLException x) {
      throw new IllegalStateException(x);
    }
  }

  private void installSchema(DataSource dataSource, SchemaContext schemaContext) {
    new FlywaySchemaInstaller().installSchema(dataSource, schemaContext);
  }

  private void migrateIfNeeded(DataSource dataSource,
                               SchemaContext schemaContext,
                               Connection connection,
                               boolean autoMigrate) throws SQLException {
    Version databaseVersion = schemaContext.getDatabaseVersion(connection);
    if (databaseVersion == null) {
      return;
    }

    Version schemaVersion = schemaContext.getSchemaVersion();
    if (databaseVersion.compareTo(schemaVersion) >= 0) {
      return;
    }

    var migrationLocator = schemaContext.getMigrationScriptLocator(connection);
    if (migrationLocator == null) {
      return;
    }

    if (autoMigrate) {
      executeMigration(dataSource, migrationLocator.getResourcePath());
    } else {
      logger.warn("Schema migration needed: database version {} is behind schema version {}. " +
                  "Set spring.jpa.schema.auto-migrate=true to migrate automatically, or run with --migrate flag",
                  databaseVersion, schemaVersion);
    }
  }

  private void executeMigration(DataSource dataSource, String migrationResourcePath) {
    logger.info("Running schema migrations from classpath:{}", migrationResourcePath);
    Flyway flyway = Flyway.configure()
                          .dataSource(dataSource)
                          .locations("classpath:" + migrationResourcePath)
                          .table("flyway_schema_history")
                          .validateOnMigrate(false)
                          .load();

    flyway.migrate();
    logger.info("Schema migration completed");
  }
}
