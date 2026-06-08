package com.stano.logging;

import org.slf4j.Logger;

import java.util.Arrays;

public class SemanticLogger {
  private final Logger logger;
  private final LoggingContext loggingContext = LoggingContext.newContext();

  public static SemanticLogger using(Logger logger) {
    return new SemanticLogger(logger);
  }

  public SemanticLogger with(String key, String value) {
    loggingContext.with(key, value);

    return this;
  }

  public SemanticLogger with(String key, int value) {
    loggingContext.with(key, Integer.toString(value));

    return this;
  }

  public SemanticLogger with(String key, long value) {
    loggingContext.with(key, Long.toString(value));

    return this;
  }

  public void debug(String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.debug(format, arguments);
    });
  }

  public void debug(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.debug(format, argumentsWithThrowable(arguments, throwable));
    });
  }

  public void info(String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.info(format, arguments);
    });
  }

  public void info(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.info(format, argumentsWithThrowable(arguments, throwable));
    });
  }

  public void warn(String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.warn(format, arguments);
    });
  }

  public void warn(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.warn(format, argumentsWithThrowable(arguments, throwable));
    });
  }

  public void error(String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.error(format, arguments);
    });
  }

  public void error(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(() -> {
      logger.error(format, argumentsWithThrowable(arguments, throwable));
    });
  }

  private Object[] argumentsWithThrowable(Object[] arguments, Throwable throwable) {
    Object[] newArguments = Arrays.copyOf(arguments, arguments.length + 1);
    newArguments[arguments.length] = throwable;
    return newArguments;
  }

  private SemanticLogger(Logger logger) {
    this.logger = logger;
  }
}
