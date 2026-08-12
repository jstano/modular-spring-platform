package com.stano.schema;

import com.stano.exceptions.RuntimeSQLException;
import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Installs or migrates a database schema using Flyway, deciding which action to take based on
 * whether the schema is already installed.
 */
public class SchemaManager {
  /**
   * Installs the schema described by {@code schemaContext} if it has not yet been installed on the
   * given datasource, or migrates it to the latest version if it has.
   *
   * @param dataSource the datasource to install or migrate the schema on
   * @param schemaContext describes the schema to install or migrate to
   * @throws RuntimeSQLException if a connection to the datasource cannot be obtained
   */
  public static void installOrMigrate(DataSource dataSource, SchemaContext schemaContext) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();

      if (schemaContext.schemaIsInstalled(connection)) {
        installer.migrateSchema(dataSource, schemaContext);
      } else {
        installer.installSchema(dataSource, schemaContext);
      }
    } catch (SQLException x) {
      throw new RuntimeSQLException(x);
    }
  }
}
