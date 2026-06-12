package com.stano.domain_jpa.schema;

import com.stano.domain_jpa.datasource.ConnectionDataSource;
import com.stano.domain_jpa.datasource.DataSourceFactory;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.StandardEnvironment;

public class SchemaMigrationLauncher {
  private static final Logger logger = LoggerFactory.getLogger(SchemaMigrationLauncher.class);
  private static final String MIGRATE_FLAG = "--migrate";

  public static boolean handleArgs(String[] args, SchemaContext schemaContext) {
    if (!hasMigrateFlag(args)) {
      return false;
    }

    logger.info("Schema migration mode: reading credentials from environment");
    StandardEnvironment environment = new StandardEnvironment();

    String url = environment.getProperty("spring.datasource.url");
    String username = environment.getProperty("spring.datasource.username");
    String password = environment.getProperty("spring.datasource.password");

    if (url == null || username == null || password == null) {
      throw new IllegalStateException(
          "Schema migration requested (--migrate flag), but required datasource properties not"
              + " found in environment. Please set: SPRING_DATASOURCE_URL,"
              + " SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD");
    }

    try (ConnectionDataSource dataSource =
        DataSourceFactory.createConnectionDataSource(url, username, password)) {
      new SchemaManager().migrate(dataSource, schemaContext);
      logger.info("Schema migration completed successfully");
      return true;
    } catch (Exception x) {
      logger.error("Schema migration failed", x);
      throw new IllegalStateException("Schema migration failed", x);
    }
  }

  private static boolean hasMigrateFlag(String[] args) {
    return Arrays.stream(args).anyMatch(arg -> arg != null && arg.equals(MIGRATE_FLAG));
  }
}
