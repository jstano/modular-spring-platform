package com.stano.schema;

import com.stano.data_source.DataSourceFactory;
import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration that installs or migrates the application's database schema on startup.
 *
 * <p>Runs before {@code com.stano.domain_jpa.datasource.JpaDataSourceAutoConfiguration} so schema
 * installation/migration completes using a lightweight, single-connection datasource before JPA's
 * own pooled datasource is created.
 */
@AutoConfiguration(beforeName = "com.stano.domain_jpa.datasource.JpaDataSourceAutoConfiguration")
@EnableConfigurationProperties(DataSourceProperties.class)
public class MigrationAutoConfiguration {
  /**
   * Creates a lightweight, single-connection {@link DataSource} from the application's standard
   * {@code spring.datasource.*} properties, and, if a {@link SchemaContext} bean is available,
   * installs or migrates the schema on it via {@link SchemaManager#installOrMigrate}.
   *
   * <p>Only active if the application has not defined its own {@code DataSource} bean. This
   * datasource is intended purely for schema installation/migration, not for application use.
   *
   * @param dataSourceProperties the standard Spring Boot datasource connection properties
   * @param schemaContextProvider provides the optional {@link SchemaContext} describing the schema
   *     to install or migrate to
   * @return the datasource used to install or migrate the schema
   */
  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(
      DataSourceProperties dataSourceProperties,
      ObjectProvider<SchemaContext> schemaContextProvider) {
    DataSource ds =
        DataSourceFactory.createConnectionDataSource(
            dataSourceProperties.getUrl(),
            dataSourceProperties.getUsername(),
            dataSourceProperties.getPassword());
    SchemaContext schemaContext = schemaContextProvider.getIfAvailable();
    if (schemaContext != null) {
      SchemaManager.installOrMigrate(ds, schemaContext);
    }
    return ds;
  }

  /**
   * Builds a default {@link SchemaContext} from the {@code msp.schema.location} (default {@code
   * db/schema.xml}) and {@code msp.schema.migration-path} (default {@code db/migration})
   * properties.
   *
   * <p>Only active if the application has not defined its own {@code SchemaContext} bean, and only
   * if the configured schema resource is present on the classpath.
   *
   * @param schemaLocation the classpath location of the schema definition resource
   * @param migrationPath the classpath location of the Flyway migration scripts
   * @return the schema context describing the schema to install or migrate to
   */
  @Bean
  @ConditionalOnMissingBean(SchemaContext.class)
  @ConditionalOnResource(resources = "classpath:${msp.schema.location:db/schema.xml}")
  public SchemaContext schemaContext(
      @Value("${msp.schema.location:db/schema.xml}") String schemaLocation,
      @Value("${msp.schema.migration-path:db/migration}") String migrationPath) {
    return new DefaultSchemaContext(
        MigrationAutoConfiguration.class.getClassLoader().getResource(schemaLocation),
        migrationPath);
  }
}
