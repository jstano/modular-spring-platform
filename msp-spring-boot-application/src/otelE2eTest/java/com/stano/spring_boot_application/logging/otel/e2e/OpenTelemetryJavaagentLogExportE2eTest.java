package com.stano.spring_boot_application.logging.otel.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.logging.SemanticLogger;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.BooleanSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Verifies that the real OpenTelemetry javaagent (attached via {@code -javaagent:}, driven only by
 * the {@code OTEL_INSTRUMENTATION_LOGBACK_APPENDER_*} env vars set on the {@code otelE2eTest}
 * Gradle task, mirroring {@code spring-launcher/Dockerfile}) auto-installs the {@code
 * otel-appender} declared in the real {@code logback-spring.xml} and exports a log record carrying
 * {@link SemanticLogger} MDC attributes. Unlike {@code OpenTelemetryAppenderMdcCaptureTest}, no
 * application code here ever calls {@code OpenTelemetryAppender.install}/{@code setOpenTelemetry} —
 * only the agent does that, exactly as in production.
 *
 * <p>Scope: the exact printed format of OTel's {@code logging} exporter (OTel javaagent 2.29.0) is
 * an unconfirmed, undocumented upstream implementation detail — empirically confirmed to write
 * plain text directly to {@code System.out} (not via {@code java.util.logging}, contrary to an
 * earlier assumption). Assertions below only check content substrings, never exact formatting. If
 * this test starts failing, check upstream agent/exporter behavior before assuming an app-side
 * regression.
 */
class OpenTelemetryJavaagentLogExportE2eTest {

  private PrintStream originalSystemOut;
  private ByteArrayOutputStream capturedOut;
  private ConfigurableApplicationContext context;

  @BeforeEach
  void setup() {
    originalSystemOut = System.out;
    capturedOut = new ByteArrayOutputStream();
    // Tee, not replace: the OTel javaagent's own startup/diagnostic output also targets
    // System.out, and losing that would make failures here much harder to diagnose.
    System.setOut(new TeeingPrintStream(originalSystemOut, capturedOut));
  }

  @AfterEach
  void cleanup() {
    System.setOut(originalSystemOut);
    if (context != null) {
      context.close();
    }
  }

  @Test
  void theRealJavaagentShouldExportALogRecordWithTheSemanticLoggerMdcAttribute() {
    // WebApplicationType.SERVLET (not NONE): the project's DefaultSpringSecurityConfig
    // auto-configuration requires an HttpSecurity bean, which only exists once Spring Security's
    // web auto-config activates under a servlet context.
    context =
        new SpringApplicationBuilder(TestApplication.class)
            .web(WebApplicationType.SERVLET)
            .properties("server.port=0")
            .profiles("prod")
            .run();

    SemanticLogger.using(LoggerFactory.getLogger(OpenTelemetryJavaagentLogExportE2eTest.class))
        .with("correlationId", "ABC123")
        .info("otel javaagent e2e probe message");

    var found =
        awaitCondition(
            Duration.ofSeconds(10),
            () -> {
              var captured = capturedOut.toString(StandardCharsets.UTF_8);
              return captured.contains("otel javaagent e2e probe message")
                  && captured.contains("correlationId=\"ABC123\"");
            });

    assertThat(found)
        .as(
            "expected stdout to contain a log record from the OTel logging exporter with both the"
                + " probe message and the correlationId MDC attribute; captured stdout: %s",
            capturedOut.toString(StandardCharsets.UTF_8))
        .isTrue();
  }

  private static boolean awaitCondition(Duration timeout, BooleanSupplier condition) {
    var deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      if (condition.getAsBoolean()) {
        return true;
      }
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
      }
    }
    return condition.getAsBoolean();
  }

  private static final class TeeingPrintStream extends PrintStream {
    private final PrintStream primary;

    TeeingPrintStream(PrintStream primary, ByteArrayOutputStream secondary) {
      super(secondary, true, StandardCharsets.UTF_8);
      this.primary = primary;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
      super.write(buf, off, len);
      primary.write(buf, off, len);
    }

    @Override
    public void flush() {
      super.flush();
      primary.flush();
    }
  }
}
