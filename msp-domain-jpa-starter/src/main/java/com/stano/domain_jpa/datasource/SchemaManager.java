package com.stano.domain_jpa.datasource;

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

/**
 * Installs or migrates the database schema on application startup, using an optional {@code
 * SchemaContext} bean supplied by the consuming application together with the {@code
 * msp.schema.auto-install}, {@code msp.schema.auto-migrate}, and {@code
 * msp.schema.skip-migration-check} configuration properties to decide what to do.
 *
 * <p>If no {@code SchemaContext} bean is available in the application context, schema management is
 * skipped entirely.
 */
public class SchemaManager {
  private static final SemanticLogger logger =
      SemanticLogger.using(LoggerFactory.getLogger(SchemaManager.class));

  /**
   * Looks up an optional {@link SchemaContext} bean in the given application context and, if
   * present, installs or migrates the schema on {@code dataSource} according to the {@code
   * msp.schema.auto-install}, {@code msp.schema.auto-migrate}, and {@code
   * msp.schema.skip-migration-check} properties. Does nothing if no {@code SchemaContext} bean is
   * available.
   *
   * @param applicationContext the application context to look up the schema context and
   *     configuration properties from
   * @param dataSource the data source to install or migrate the schema on
   */
  public static void migrate(ApplicationContext applicationContext, DataSource dataSource) {
    var schemaContext = applicationContext.getBeanProvider(SchemaContext.class).getIfAvailable();

    if (schemaContext != null) {
      boolean autoInstall =
          applicationContext
              .getEnvironment()
              .getProperty("msp.schema.auto-install", Boolean.class, false);
      boolean autoMigrate =
          applicationContext
              .getEnvironment()
              .getProperty("msp.schema.auto-migrate", Boolean.class, false);
      boolean skipMigrationCheck =
          applicationContext
              .getEnvironment()
              .getProperty("msp.schema.skip-migration-check", Boolean.class, false);
      installOrMigrate(dataSource, schemaContext, autoInstall, autoMigrate, skipMigrationCheck);
    }
  }

  /**
   * Installs, migrates, or verifies the schema on {@code dataSource} depending on its current state
   * and the given flags: installs the schema if it is missing and {@code autoInstall} is set;
   * migrates it if already installed and {@code autoMigrate} is set; otherwise checks for pending
   * migrations unless {@code skipMigrationCheck} is set.
   *
   * @param dataSource the data source to install or migrate the schema on
   * @param schemaContext describes the schema to install/migrate and how to check its state
   * @param autoInstall whether to automatically install the schema when it is not yet installed
   * @param autoMigrate whether to automatically run pending migrations when the schema is already
   *     installed
   * @param skipMigrationCheck whether to skip failing startup when pending migrations exist and
   *     {@code autoMigrate} is not set
   * @throws IllegalStateException if the schema is not installed and {@code autoInstall} is not
   *     set, or if pending migrations exist and neither {@code autoMigrate} nor {@code
   *     skipMigrationCheck} is set
   * @throws RuntimeSQLException if a {@link SQLException} occurs while communicating with the
   *     database
   */
  public static void installOrMigrate(
      DataSource dataSource,
      SchemaContext schemaContext,
      boolean autoInstall,
      boolean autoMigrate,
      boolean skipMigrationCheck) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();
      if (!schemaContext.schemaIsInstalled(connection)) {
        if (autoInstall) {
          installer.installSchema(dataSource, schemaContext);
        } else {
          throw new IllegalStateException(
              "Database schema is not installed and msp.schema.auto-install is not enabled. "
                  + "Run the schema installation manually or enable msp.schema.auto-install.");
        }
      } else if (autoMigrate) {
        installer.migrateSchema(dataSource, schemaContext);
      } else if (skipMigrationCheck) {
        logger.info("Skipping pending migration check (msp.schema.skip-migration-check=true)");
      } else {
        List<String> pendingMigrations = installer.getPendingMigrations(dataSource, schemaContext);
        if (!pendingMigrations.isEmpty()) {
          logger
              .with("pendingMigrations", pendingMigrations.toString())
              .error(
                  "Database schema has pending migrations but msp.schema.auto-migrate is "
                      + "not enabled");
          throw new IllegalStateException(
              "Database schema has pending migrations but msp.schema.auto-migrate is not "
                  + "enabled: "
                  + pendingMigrations
                  + ". Run the migrations manually or enable msp.schema.auto-migrate.");
        }
      }
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }

  /**
   * Installs the schema described by {@code schemaContext} on {@code dataSource} if it is not
   * already installed. Unlike {@link #installOrMigrate}, this performs the install unconditionally,
   * without consulting the {@code msp.schema.*} configuration flags.
   *
   * @param dataSource the data source to install the schema on
   * @param schemaContext describes the schema to install and how to check its state
   * @throws RuntimeSQLException if a {@link SQLException} occurs while communicating with the
   *     database
   */
  public static void installSchema(DataSource dataSource, SchemaContext schemaContext) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();
      if (!schemaContext.schemaIsInstalled(connection)) {
        installer.installSchema(dataSource, schemaContext);
      }
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }
}
