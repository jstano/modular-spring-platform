package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.EnableJpa;
import com.stano.domain_jpa.PostgresJpaTestConfig;
import com.stano.schema.installer.schemacontext.SchemaContext;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpa
public class PostgresIntegrationTestConfig extends PostgresJpaTestConfig {
  @Bean
  public SchemaContext schemaContext() {
    return new TestSchemaContext();
  }
}
