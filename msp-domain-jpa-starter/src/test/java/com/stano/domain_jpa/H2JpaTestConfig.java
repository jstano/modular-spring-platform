package com.stano.domain_jpa;

import com.stano.domain_jpa.springdata.EntityRepositoryImpl;
import com.stano.logging.SemanticLogger;
import javax.sql.DataSource;
import org.h2.Driver;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.stano.domain_jpa")
@EnableJpaRepositories(
    basePackages = "com.stano.domain_jpa",
    repositoryBaseClass = EntityRepositoryImpl.class)
public class H2JpaTestConfig {
  private static final SemanticLogger logger =
      SemanticLogger.using(LoggerFactory.getLogger(H2JpaTestConfig.class));

  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(ApplicationContext applicationContext) {
    DataSource dataSource =
        DataSourceBuilder.create()
            .driverClassName(Driver.class.getName())
            .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
            .username("sa")
            .password("")
            .build();

    logger.info("Skipping schema installation for H2 in-memory database");
    return dataSource;
  }
}
