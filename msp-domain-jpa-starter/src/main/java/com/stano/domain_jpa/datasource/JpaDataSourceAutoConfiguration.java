package com.stano.domain_jpa.datasource;

import com.stano.domain_jpa.schema.SchemaManager;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
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
}
