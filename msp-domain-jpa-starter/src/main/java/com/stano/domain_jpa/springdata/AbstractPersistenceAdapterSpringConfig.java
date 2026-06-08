package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.springjdbc.hikari.HikariDataSourceFactory;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public abstract class AbstractPersistenceAdapterSpringConfig {
  @Bean
  public DataSource dataSource(ApplicationContext applicationContext) {
    DataSource dataSource = createDataSource(applicationContext);

    initializeDataSource(dataSource);
    dataSourceReady(dataSource);

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager(Environment environment, DataSource dataSource) {
    return createTransactionManager(environment, dataSource);
  }

  protected abstract PlatformTransactionManager createTransactionManager(Environment environment, DataSource dataSource);

  protected DataSource createDataSource(ApplicationContext applicationContext) {
    Environment environment = applicationContext.getEnvironment();
    DataSourceProperties dataSourceProperties = applicationContext.getBean(DataSourceProperties.class);

    return HikariDataSourceFactory.createDataSource(environment,
                                                    dataSourceProperties.getUrl(),
                                                    dataSourceProperties.getUsername(),
                                                    dataSourceProperties.getPassword());
  }

  protected void initializeDataSource(DataSource dataSource) {
  }

  protected void dataSourceReady(DataSource dataSource) {
  }
}
