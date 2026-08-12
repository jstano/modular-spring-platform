package com.stano.spring_boot_application.error;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration that registers {@link GlobalExceptionHandler} for servlet-based web
 * applications.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalExceptionHandlerAutoConfiguration {
  /**
   * Creates the platform's {@link GlobalExceptionHandler} bean if the application has not already
   * defined one.
   *
   * @return a new {@code GlobalExceptionHandler}
   */
  @Bean
  @ConditionalOnMissingBean(GlobalExceptionHandler.class)
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
