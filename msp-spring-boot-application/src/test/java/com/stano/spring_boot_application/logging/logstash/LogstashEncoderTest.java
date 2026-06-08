package com.stano.spring_boot_application.logging.logstash;

import ch.qos.logback.classic.LoggerContext;
import com.stano.logging.SemanticLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;

class LogstashEncoderTest {

    private PrintStream saveSystemOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() throws UnknownHostException {
        initializeLoggingContext();
        LoggerFactory.getLogger(LogstashEncoderTest.class);

        saveSystemOut = System.out;

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    private void initializeLoggingContext() {
        Logger logger = LoggerFactory.getLogger(LogstashEncoderTest.class);
        if (logger instanceof ch.qos.logback.classic.Logger) {
            LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();
            if (loggerContext != null && loggerContext.getProperty("HOSTNAME") == null) {
                try {
                    loggerContext.putProperty("HOSTNAME", InetAddress.getLocalHost().getHostName());
                } catch (UnknownHostException x) {
                    loggerContext.putProperty("HOSTNAME", "UNKNOWN_HOST");
                }
            }
        }
    }

    @AfterEach
    void cleanup() {
        System.setOut(saveSystemOut);
    }

    @Test
    void theSemanticLoggerShouldOutputTheAdditionalKeyValuePairs() throws Exception {
        var logger = LoggerFactory.getLogger(LogstashEncoderTest.class);

        SemanticLogger.using(logger)
                    .with("correlationId", "ABC123")
                    .with("sessionId", "XYZ456")
                    .info("This is a test message");

        var jsonOutput = out.toString();

        assertThat(jsonOutput).contains("\"@timestamp\"");
        assertThat(jsonOutput).contains("\"@version\":\"1\"");
        assertThat(jsonOutput).contains("\"message\":\"This is a test message\"");
        assertThat(jsonOutput).contains("\"logger_name\":\"" + LogstashEncoderTest.class.getName() + "\"");
        assertThat(jsonOutput).contains("\"thread_name\":\"" + Thread.currentThread().getName() + "\"");
        assertThat(jsonOutput).contains("\"level\":\"INFO\"");
        assertThat(jsonOutput).contains("\"level_value\":20000");
        assertThat(jsonOutput).contains("\"HOSTNAME\":\"" + InetAddress.getLocalHost().getHostName() + "\"");
        assertThat(jsonOutput).contains("\"correlationId\":\"ABC123\"");
        assertThat(jsonOutput).contains("\"sessionId\":\"XYZ456\"");
    }

    @Test
    void theStandardLoggerShouldOutputTheMessagesInJsonFormat() throws Exception {
        var logger = LoggerFactory.getLogger(LogstashEncoderTest.class);

        logger.info("This is a test message");

        var jsonOutput = out.toString();

        assertThat(jsonOutput).contains("\"@timestamp\"");
        assertThat(jsonOutput).contains("\"@version\":\"1\"");
        assertThat(jsonOutput).contains("\"message\":\"This is a test message\"");
        assertThat(jsonOutput).contains("\"logger_name\":\"" + LogstashEncoderTest.class.getName() + "\"");
        assertThat(jsonOutput).contains("\"thread_name\":\"" + Thread.currentThread().getName() + "\"");
        assertThat(jsonOutput).contains("\"level\":\"INFO\"");
        assertThat(jsonOutput).contains("\"level_value\":20000");
        assertThat(jsonOutput).contains("\"HOSTNAME\":\"" + InetAddress.getLocalHost().getHostName() + "\"");
    }
}
