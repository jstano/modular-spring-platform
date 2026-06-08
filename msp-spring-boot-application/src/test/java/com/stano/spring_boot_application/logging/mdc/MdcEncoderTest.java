package com.stano.spring_boot_application.logging.mdc;

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

class MdcEncoderTest {

    private PrintStream saveSystemOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() throws UnknownHostException {
        initializeLoggingContext();
        LoggerFactory.getLogger(MdcEncoderTest.class);

        saveSystemOut = System.out;

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    private void initializeLoggingContext() {
        Logger logger = LoggerFactory.getLogger(MdcEncoderTest.class);
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
    void theSemanticLoggerShouldOutputTheAdditionalKeyValuePairs() throws UnknownHostException {
        var logger = LoggerFactory.getLogger(MdcEncoderTest.class);
        var hostname = InetAddress.getLocalHost().getHostName();

        SemanticLogger.using(logger)
                    .with("correlationId", "ABC123")
                    .with("sessionId", "XYZ456")
                    .info("This is a test message");

        assertThat(out.toString()).isEqualTo("correlationId: ABC123, sessionId: XYZ456, hostname: " + hostname + " - This is a test message");
    }

    @Test
    void theStandardLoggerShouldOutputTheMessageWithoutTheAdditionalKeyValuePairs() throws UnknownHostException {
        var logger = LoggerFactory.getLogger(MdcEncoderTest.class);
        var hostname = InetAddress.getLocalHost().getHostName();

        logger.info("This is a test message");

        assertThat(out.toString()).isEqualTo("correlationId: , sessionId: , hostname: " + hostname + " - This is a test message");
    }
}
