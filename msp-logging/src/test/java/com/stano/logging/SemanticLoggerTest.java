package com.stano.logging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

class SemanticLoggerTest {

  @BeforeEach
  void setup() {
    MDC.put("key1", "abc");
    MDC.put("key2", "xyz");
    MDC.put("key3", "jhg");
  }

  @AfterEach
  void cleanup() {
    MDC.remove("key1");
    MDC.remove("key2");
    MDC.remove("key3");
  }

  @Test
  void debugShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();

    semanticLogger.debug("{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key2")).isEqualTo("xyz");
    assertThat(MDC.get("key3")).isEqualTo("jhg");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void debugWithExceptionShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var exception = new Exception("test");
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.debug(exception, "{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void infoShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.info("{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void infoWithExceptionShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var exception = new Exception("test");
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.info(exception, "{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void warnShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.warn("{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void warnWithExceptionShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var exception = new Exception("test");
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.warn(exception, "{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void errorShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.error("{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }

  @Test
  void errorWithExceptionShouldSetAndRestoreMdc() {
    var logger = LoggerFactory.getLogger(SemanticLoggerTest.class);
    var exception = new Exception("test");
    var semanticLogger =
        SemanticLogger.using(logger).with("key1", "ABC").with("key2", "XYZ").with("key4", "YTR");

    semanticLogger.error(exception, "{} - {}", 1, 2);

    assertThat(MDC.get("key1")).isEqualTo("abc");
    assertThat(MDC.get("key4")).isNull();
  }
}
