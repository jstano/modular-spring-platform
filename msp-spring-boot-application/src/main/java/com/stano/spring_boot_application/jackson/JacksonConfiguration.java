package com.stano.spring_boot_application.jackson;

import com.stano.jackson.ObjectMapperFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

/**
 * Auto-configuration that exposes the platform's preconfigured Jackson {@link ObjectMapper} as a
 * Spring bean.
 */
@AutoConfiguration
public class JacksonConfiguration {
  /**
   * Provides the shared {@link ObjectMapperFactory} instance as the application's {@code
   * ObjectMapper} bean, unless the application has already defined one.
   *
   * @return the platform's preconfigured {@code ObjectMapper}
   */
  @Bean
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper objectMapper() {
    return ObjectMapperFactory.getInstance();
  }
}
