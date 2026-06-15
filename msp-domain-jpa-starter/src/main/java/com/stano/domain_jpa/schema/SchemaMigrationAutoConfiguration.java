package com.stano.domain_jpa.schema;

import com.stano.domain_jpa.datasource.ConnectionDataSource;
import com.stano.domain_jpa.datasource.DataSourceFactory;
import com.stano.schema.installer.flyway.FlywaySchemaInstaller;
import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.spring_boot_application.schema.SchemaMigrationRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(SchemaMigrationRequestedEvent.class)
public class SchemaMigrationAutoConfiguration {
  private static final Logger logger =
      LoggerFactory.getLogger(SchemaMigrationAutoConfiguration.class);

  @Bean
  @ConditionalOnBean(SchemaContext.class)
  public ApplicationListener<SchemaMigrationRequestedEvent> schemaMigrationListener(
      DataSourceProperties dataSourceProperties, SchemaContext schemaContext) {
    return event -> {
      logger.info("Handling schema migration request");

      String url = dataSourceProperties.getUrl();
      String username = dataSourceProperties.getUsername();
      String password = dataSourceProperties.getPassword();

      if (url == null || username == null || password == null) {
        throw new IllegalStateException(
            "Schema migration requested but datasource properties not configured. Ensure"
                + " spring.datasource.url, spring.datasource.username, and"
                + " spring.datasource.password are set.");
      }

      try (ConnectionDataSource dataSource =
          DataSourceFactory.createConnectionDataSource(url, username, password)) {
        new FlywaySchemaInstaller().migrateSchema(dataSource, schemaContext);
        event.markHandled();
      } catch (Exception x) {
        logger.error("Schema migration failed", x);
        throw new IllegalStateException("Schema migration failed", x);
      }
    };
  }
}
