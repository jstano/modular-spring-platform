package com.stano.domain_jpa.schema;

import com.stano.exceptions.RuntimeSQLException;
import com.stano.logging.SemanticLogger;
import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class SchemaManager {
  private static final SemanticLogger logger =
      SemanticLogger.using(LoggerFactory.getLogger(SchemaManager.class));

  public static void migrate(ApplicationContext applicationContext, DataSource dataSource) {
    var schemaContext = applicationContext.getBeanProvider(SchemaContext.class).getIfAvailable();

    if (schemaContext != null) {
      boolean autoMigrate =
          applicationContext
              .getEnvironment()
              .getProperty("msp.jpa.schema.auto-migrate", Boolean.class, false);
      boolean skipMigrationCheck =
          applicationContext
              .getEnvironment()
              .getProperty("msp.jpa.schema.skip-migration-check", Boolean.class, false);
      installOrMigrate(dataSource, schemaContext, autoMigrate, skipMigrationCheck);
    }
  }

  public static void installOrMigrate(
      DataSource dataSource,
      SchemaContext schemaContext,
      boolean autoMigrate,
      boolean skipMigrationCheck) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();
      if (!schemaContext.schemaIsInstalled(connection)) {
        installer.installSchema(dataSource, schemaContext);
      } else if (autoMigrate) {
        installer.migrateSchema(dataSource, schemaContext);
      } else if (skipMigrationCheck) {
        logger.info("Skipping pending migration check (msp.jpa.schema.skip-migration-check=true)");
      } else {
        List<String> pendingMigrations = installer.getPendingMigrations(dataSource, schemaContext);
        if (!pendingMigrations.isEmpty()) {
          logger
              .with("pendingMigrations", pendingMigrations.toString())
              .error(
                  "Database schema has pending migrations but msp.jpa.schema.auto-migrate is "
                      + "not enabled");
          throw new IllegalStateException(
              "Database schema has pending migrations but msp.jpa.schema.auto-migrate is not "
                  + "enabled: "
                  + pendingMigrations
                  + ". Run the migrations manually or enable msp.jpa.schema.auto-migrate.");
        }
      }
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }

  public static void installOrMigrate(DataSource dataSource, SchemaContext schemaContext) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();

      if (!schemaContext.schemaIsInstalled(connection)) {
        installer.installSchema(dataSource, schemaContext);
      }

      installer.migrateSchema(dataSource, schemaContext);
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }
}
