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

/**
 * Auto-configuration applying platform-wide application startup/shutdown behavior: forcing the
 * default JVM timezone to UTC, and logging a summary banner on startup and a message on shutdown.
 */
@AutoConfiguration
public class SpringApplicationAutoConfiguration {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(SpringApplicationAutoConfiguration.class);

  /**
   * Sets the JVM's default {@link TimeZone} to UTC, so that all date/time handling that relies on
   * the platform default is timezone-consistent regardless of the host machine's locale.
   */
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(java.time.ZoneOffset.UTC));
  }

  /**
   * Provides a listener that logs a message when the application context is closed.
   *
   * @return a listener that logs on {@link ContextClosedEvent}
   */
  @Bean
  public ApplicationListener<ContextClosedEvent> contextClosedListener() {
    return event -> LOGGER.info("Shutting down...");
  }

  /**
   * Provides a listener that logs a startup summary banner, including the time taken to start and
   * the local server port, once the application is fully ready to service requests.
   *
   * @return a listener that logs on {@link ApplicationReadyEvent}
   */
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
