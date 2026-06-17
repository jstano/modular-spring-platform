package com.stano.domain_jpa.entity;

import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JpaTestSchemaConfig {
  @Bean
  public SchemaContext schemaContext() {
    return new DefaultSchemaContext(
        JpaTestSchemaConfig.class.getClassLoader().getResource("db/test-schema.xml"));
  }
}
