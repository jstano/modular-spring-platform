package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.EnableJpa;
import com.stano.domain_jpa.H2JpaTestConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJpa(entityPackages = {TestEntity.class, com.stano.domain_jpa.entity.TestEntity.class})
public class TestConfig extends H2JpaTestConfig {
  @Bean
  public MeterRegistry meterRegistry() {
    return new SimpleMeterRegistry();
  }
}
