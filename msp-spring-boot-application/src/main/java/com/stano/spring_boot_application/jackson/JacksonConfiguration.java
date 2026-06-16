package com.stano.spring_boot_application.jackson;

import com.stano.jackson.ObjectMapperFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
public class JacksonConfiguration {
  @Bean
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper() {
    return ObjectMapperFactory.getInstance();
  }
}
