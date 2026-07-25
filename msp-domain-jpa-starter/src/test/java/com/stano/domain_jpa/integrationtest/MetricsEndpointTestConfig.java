package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.EnableJpa;
import com.stano.domain_jpa.PostgresJpaTestConfig;
import com.stano.schema.MigrationAutoConfiguration;
import com.stano.schema.installer.schemacontext.SchemaContext;
import java.sql.Connection;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {PostgresJpaTestConfig.class, MigrationAutoConfiguration.class})
@EnableJpa(entityPackages = TestEntity.class)
@EnableJpaRepositories(
    basePackageClasses = TestEntityRepository.class,
    repositoryBaseClass = com.stano.domain_jpa.springdata.EntityRepositoryImpl.class)
public class MetricsEndpointTestConfig {
  @Bean
  public SchemaContext schemaContext() throws java.sql.SQLException {
    SchemaContext schemaContext = Mockito.mock(SchemaContext.class);
    Mockito.when(schemaContext.schemaIsInstalled(Mockito.any(Connection.class))).thenReturn(true);
    return schemaContext;
  }
}
