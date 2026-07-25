package com.stano.spring_boot_application.logging.otel;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.stano.logging.SemanticLogger;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.SimpleLogRecordProcessor;
import io.opentelemetry.sdk.testing.exporter.InMemoryLogRecordExporter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

/**
 * Verifies the {@code opentelemetry-logback-appender-1.0} library contract: an {@link
 * OpenTelemetryAppender} configured with {@code captureMdcAttributes=*} forwards MDC key/value
 * pairs as attributes on the exported {@code LogRecordData}.
 *
 * <p>Scope: this wires the appender programmatically in-process via {@link
 * OpenTelemetryAppender#setOpenTelemetry}. It does NOT exercise the real deployed path, where the
 * OTel javaagent (not application code) auto-installs {@code otel-appender} from {@code
 * logback-spring.xml} and reads the {@code OTEL_INSTRUMENTATION_LOGBACK_APPENDER_*} env vars set in
 * {@code spring-launcher/Dockerfile}. This test only proves the underlying library mechanism works
 * when configured to capture MDC attributes — it is not coverage of the agent/env-var wiring
 * itself.
 */
class OpenTelemetryAppenderMdcCaptureTest {

  private LoggerContext loggerContext;
  private InMemoryLogRecordExporter exporter;
  private OpenTelemetrySdk openTelemetrySdk;

  @BeforeEach
  void setup() {
    loggerContext = new LoggerContext();
    // A freshly constructed LoggerContext has no MDCAdapter bound (that normally happens once
    // for the JVM's default context via SLF4J's ContextInitializer). It must be the *same*
    // adapter instance org.slf4j.MDC delegates to globally, since SemanticLogger/LoggingContext
    // populate MDC via the static org.slf4j.MDC API, not through this LoggerContext.
    loggerContext.setMDCAdapter(MDC.getMDCAdapter());

    exporter = InMemoryLogRecordExporter.create();
    var loggerProvider =
        SdkLoggerProvider.builder()
            .addLogRecordProcessor(SimpleLogRecordProcessor.create(exporter))
            .build();
    openTelemetrySdk = OpenTelemetrySdk.builder().setLoggerProvider(loggerProvider).build();
  }

  @AfterEach
  void cleanup() {
    openTelemetrySdk.close();
    loggerContext.stop();
  }

  @Test
  void theSemanticLoggerMdcAttributesShouldAppearOnTheExportedLogRecord() {
    var appender = new OpenTelemetryAppender();
    appender.setContext(loggerContext);
    appender.setCaptureMdcAttributes("*");
    appender.setOpenTelemetry(openTelemetrySdk);
    appender.start();

    var logbackLogger = loggerContext.getLogger(OpenTelemetryAppenderMdcCaptureTest.class);
    logbackLogger.addAppender(appender);
    logbackLogger.setLevel(Level.INFO);

    SemanticLogger.using(logbackLogger)
        .with("correlationId", "ABC123")
        .with("sessionId", "XYZ456")
        .info("This is a test message");

    var finishedLogRecords = exporter.getFinishedLogRecordItems();
    assertThat(finishedLogRecords).hasSize(1);

    var attributes = finishedLogRecords.getFirst().getAttributes();
    assertThat(attributes.get(AttributeKey.stringKey("correlationId"))).isEqualTo("ABC123");
    assertThat(attributes.get(AttributeKey.stringKey("sessionId"))).isEqualTo("XYZ456");
  }
}
