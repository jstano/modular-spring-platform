package com.stano.domain_jpa.datasource;

import com.stano.data_source.DataSourceFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Auto-configures the application {@link DataSource} used by JPA, and triggers schema
 * install/migration checks against it before it is exposed as a bean.
 */
@AutoConfiguration
@EnableConfigurationProperties(DataSourceProperties.class)
public class JpaDataSourceAutoConfiguration {
  /**
   * Creates the application's {@link DataSource} from the configured {@code spring.datasource}
   * properties, then delegates to {@link SchemaManager#migrate} to install or verify the database
   * schema before the data source is used. Only created when no other {@link DataSource} bean is
   * already present.
   *
   * @param environment the Spring environment, used to build the underlying data source
   * @param dataSourceProperties the bound {@code spring.datasource} properties (url, username,
   *     password)
   * @param applicationContext the application context, used to look up an optional {@code
   *     SchemaContext} bean for schema install/migration
   * @return the configured, schema-checked application data source
   */
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
