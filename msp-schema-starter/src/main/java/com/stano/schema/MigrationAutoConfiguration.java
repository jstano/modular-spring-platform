package com.stano.schema;

import com.stano.data_source.DataSourceFactory;
import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(beforeName = "com.stano.domain_jpa.datasource.JpaDataSourceAutoConfiguration")
@EnableConfigurationProperties(DataSourceProperties.class)
public class MigrationAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(DataSourceProperties dataSourceProperties) {
    return DataSourceFactory.createConnectionDataSource(
        dataSourceProperties.getUrl(),
        dataSourceProperties.getUsername(),
        dataSourceProperties.getPassword());
  }

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

  @Bean
  @ConditionalOnBean(SchemaContext.class)
  public SchemaMigrationInitializer schemaMigrationInitializer(
      DataSource dataSource, SchemaContext schemaContext) {
    return new SchemaMigrationInitializer(dataSource, schemaContext);
  }

  static class SchemaMigrationInitializer {
    private final DataSource dataSource;
    private final SchemaContext schemaContext;

    SchemaMigrationInitializer(DataSource dataSource, SchemaContext schemaContext) {
      this.dataSource = dataSource;
      this.schemaContext = schemaContext;
    }

    @PostConstruct
    void migrate() {
      SchemaManager.installOrMigrate(dataSource, schemaContext);
    }
  }
}
