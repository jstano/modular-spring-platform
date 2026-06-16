package com.stano.spring_boot_application;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;

@AutoConfiguration
public class SpringApplicationAutoConfiguration {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(SpringApplicationAutoConfiguration.class);

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(java.time.ZoneOffset.UTC));
  }

  @Bean
  public ApplicationListener<ContextClosedEvent> contextClosedListener() {
    return event -> LOGGER.info("Shutting down...");
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyListener() {
    return event -> {
      Environment environment = event.getApplicationContext().getEnvironment();
      Duration timeTaken = event.getTimeTaken();

      LOGGER.info(
          """
          **************************************************
          Application started in {} seconds
          Application is ready on port {}
          **************************************************\
          """,
          String.format("%.3f", timeTaken.toMillis() / 1000.0),
          environment.getProperty("local.server.port", Integer.class));
    };
  }
}
