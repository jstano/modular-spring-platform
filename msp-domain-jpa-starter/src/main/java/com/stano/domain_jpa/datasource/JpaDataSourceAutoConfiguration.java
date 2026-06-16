package com.stano.domain_jpa.datasource;

import com.stano.domain_jpa.schema.SchemaManager;
import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@EnableConfigurationProperties(DataSourceProperties.class)
public class JpaDataSourceAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(
      Environment environment,
      DataSourceProperties dataSourceProperties,
      ApplicationContext applicationContext) {
    DataSource dataSource =
        DataSourceFactory.createDataSource(
            environment,
            dataSourceProperties.getUrl(),
            dataSourceProperties.getUsername(),
            dataSourceProperties.getPassword());
    SchemaManager.migrate(applicationContext, dataSource);
    return dataSource;
  }

  @Bean
  @ConditionalOnMissingBean(SchemaContext.class)
  @ConditionalOnResource(resources = "classpath:${msp.jpa.schema.location:schema.xml}")
  public SchemaContext schemaContext(
      @Value("${msp.jpa.schema.location:schema.xml}") String schemaLocation,
      @Value("${msp.jpa.schema.migration-path:db/migration}") String migrationPath) {
    return new DefaultSchemaContext(
        JpaDataSourceAutoConfiguration.class.getClassLoader().getResource(schemaLocation),
        migrationPath);
  }
}
