package org.springframework.boot.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import org.springframework.boot.logging.LoggingInitializationContext;

/**
 * {@link SpringBootJoranConfigurator} is package-private, so this same-package shim exists purely
 * to expose it to {@code LogbackSpringXmlProfileWiringTest} in our own test sources.
 */
public final class TestSpringBootJoranConfiguratorFactory {

  public static JoranConfigurator create(LoggingInitializationContext initializationContext) {
    return new SpringBootJoranConfigurator(initializationContext);
  }

  private TestSpringBootJoranConfiguratorFactory() {}
}
