package com.stano.spring_boot_application.logging.mdc;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.logging.SemanticLogger;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class MdcEncoderTest {

  private PrintStream saveSystemOut;
  private ByteArrayOutputStream out;

  @BeforeEach
  void setup() throws UnknownHostException {
    LoggerFactory.getLogger(MdcEncoderTest.class);

    saveSystemOut = System.out;

    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
  }

  @AfterEach
  void cleanup() {
    System.setOut(saveSystemOut);
  }

  @Test
  void theSemanticLoggerShouldOutputTheAdditionalKeyValuePairs() throws UnknownHostException {
    var logger = LoggerFactory.getLogger(MdcEncoderTest.class);
    var hostname = InetAddress.getLocalHost().getCanonicalHostName();

    SemanticLogger.using(logger)
        .with("correlationId", "ABC123")
        .with("sessionId", "XYZ456")
        .info("This is a test message");

    assertThat(out.toString())
        .isEqualTo(
            "correlationId: ABC123, sessionId: XYZ456, hostname: "
                + hostname
                + " - This is a test message");
  }

  @Test
  void theStandardLoggerShouldOutputTheMessageWithoutTheAdditionalKeyValuePairs()
      throws UnknownHostException {
    var logger = LoggerFactory.getLogger(MdcEncoderTest.class);
    var hostname = InetAddress.getLocalHost().getCanonicalHostName();

    logger.info("This is a test message");

    assertThat(out.toString())
        .isEqualTo(
            "correlationId: , sessionId: , hostname: " + hostname + " - This is a test message");
  }
}
