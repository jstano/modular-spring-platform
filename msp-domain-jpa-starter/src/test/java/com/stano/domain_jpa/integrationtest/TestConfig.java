package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.EnableJpa;
import com.stano.domain_jpa.PostgresJpaTestConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.stano.domain_jpa")
@EnableJpa(entityPackages = {TestEntity.class, com.stano.domain_jpa.entity.TestEntity.class})
@EnableJpaRepositories(
    basePackages = "com.stano.domain_jpa",
    repositoryBaseClass = com.stano.domain_jpa.springdata.EntityRepositoryImpl.class)
@Import(PostgresJpaTestConfig.class)
public class TestConfig {
  @Bean
  public MeterRegistry meterRegistry() {
    return new SimpleMeterRegistry();
  }
}
