package com.stano.spring_boot_application.logging;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.logstash.logback.encoder.LogstashEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.TestSpringBootJoranConfiguratorFactory;
import org.springframework.core.env.StandardEnvironment;

/**
 * Parses the real {@code logback-spring.xml} and asserts which appenders each {@code
 * <springProfile>} block wires to the root logger. Regression guard for a bug where the {@code
 * local} and {@code !local} blocks had their appenders swapped, leaving deployed containers (which
 * always run {@code !local}) with plain-text logging and no OTel export. Scope: this only exercises
 * Joran XML parsing and profile selection, not Spring Boot's real bootstrap/{@code LoggingSystem}
 * initialization order.
 */
class LogbackSpringXmlProfileWiringTest {

  private static final URL LOGBACK_SPRING_XML =
      LogbackSpringXmlProfileWiringTest.class.getResource("/logback-spring.xml");

  private LoggerContext context;

  @AfterEach
  void cleanup() {
    if (context != null) {
      context.stop();
    }
  }

  @Test
  void localProfileShouldWireOnlyThePlainConsoleAppender() throws Exception {
    var root = configureFor("local");

    assertThat(appenderNames(root)).containsExactly("console-appender");
    assertThat(root.getAppender("console-appender")).isInstanceOf(ConsoleAppender.class);
  }

  @Test
  void nonLocalProfileShouldWireTheJsonAndOtelAppenders() throws Exception {
    var root = configureFor("prod");

    assertThat(appenderNames(root)).containsExactlyInAnyOrder("json-appender", "otel-appender");

    var jsonAppender = root.getAppender("json-appender");
    assertThat(jsonAppender).isInstanceOf(ConsoleAppender.class);
    assertThat(((ConsoleAppender<?>) jsonAppender).getEncoder())
        .isInstanceOf(LogstashEncoder.class);

    assertThat(root.getAppender("otel-appender")).isInstanceOf(OpenTelemetryAppender.class);
  }

  private Logger configureFor(String activeProfile) throws Exception {
    context = new LoggerContext();
    context.reset();

    var environment = new StandardEnvironment();
    environment.setActiveProfiles(activeProfile);

    var configurator =
        TestSpringBootJoranConfiguratorFactory.create(
            new LoggingInitializationContext(environment));
    configurator.setContext(context);
    configurator.doConfigure(LOGBACK_SPRING_XML);

    return context.getLogger(Logger.ROOT_LOGGER_NAME);
  }

  private static List<String> appenderNames(Logger logger) {
    var names = new ArrayList<String>();
    for (var it = logger.iteratorForAppenders(); it.hasNext(); ) {
      Appender<?> appender = it.next();
      names.add(appender.getName());
    }
    return names;
  }
}
