package com.stano.spring_boot_application.error;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionHandlerAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(GlobalExceptionHandler.class)
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
