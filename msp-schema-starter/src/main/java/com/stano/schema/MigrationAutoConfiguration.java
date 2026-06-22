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

@AutoConfiguration(beforeName = "com.stano.domain_jpa.datasource.JpaDataSourceAutoConfiguration")
@EnableConfigurationProperties(DataSourceProperties.class)
public class MigrationAutoConfiguration {
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
