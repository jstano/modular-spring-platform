package com.stano.schema;

import com.stano.exceptions.RuntimeSQLException;
import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class SchemaManager {
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
