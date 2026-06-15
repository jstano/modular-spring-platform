package com.stano.domain_jpa.schema;

import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;

public class SchemaManager {
  public static void migrate(ApplicationContext applicationContext, DataSource dataSource) {
    var schemaContext = applicationContext.getBeanProvider(SchemaContext.class).getIfAvailable();

    if (schemaContext != null) {
      boolean autoMigrate =
          applicationContext
              .getEnvironment()
              .getProperty("msp.jpa.schema.auto-migrate", Boolean.class, false);
      installOrMigrate(dataSource, schemaContext, autoMigrate);
    }
  }

  private static void installOrMigrate(
      DataSource dataSource, SchemaContext schemaContext, boolean autoMigrate) {
    try (Connection connection = dataSource.getConnection()) {
      FlywaySchemaInstaller installer = new FlywaySchemaInstaller();
      if (!schemaContext.schemaIsInstalled(connection)) {
        installer.installSchema(dataSource, schemaContext);
      } else if (autoMigrate) {
        installer.migrateSchema(dataSource, schemaContext);
      }
    } catch (SQLException x) {
      throw new IllegalStateException(x);
    }
  }
}
