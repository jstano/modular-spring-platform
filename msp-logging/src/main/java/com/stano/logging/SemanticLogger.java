package com.stano.logging;

import java.util.Arrays;
import org.slf4j.Logger;

/**
 * Fluent wrapper around SLF4J {@link org.slf4j.Logger} for structured, key/value log messages.
 *
 * <p>Example: {@code SemanticLogger.using(logger).with("orderId", id).info("Order placed")}.
 *
 * <p>Key/value pairs added via {@link #with(String, String)} (and its overloads) are applied to the
 * SLF4J {@link org.slf4j.MDC} only for the duration of the log call that follows, via {@link
 * LoggingContext}, and are cleared afterwards — they do not leak into subsequent log statements.
 */
public class SemanticLogger {
  private final Logger logger;
  private final LoggingContext loggingContext = LoggingContext.newContext();

  /**
   * Starts a fluent logging call for the given SLF4J logger.
   *
   * @param logger the underlying logger to delegate to
   * @return a new semantic logger builder
   */
  public static SemanticLogger using(Logger logger) {
    return new SemanticLogger(logger);
  }

  /**
   * Adds a string key/value pair to be included, via MDC, with the next log call.
   *
   * @param key the MDC key
   * @param value the MDC value
   * @return this logger, for chaining
   */
  public SemanticLogger with(String key, String value) {
    loggingContext.with(key, value);

    return this;
  }

  /**
   * Adds an integer key/value pair to be included, via MDC, with the next log call.
   *
   * @param key the MDC key
   * @param value the MDC value
   * @return this logger, for chaining
   */
  public SemanticLogger with(String key, int value) {
    loggingContext.with(key, Integer.toString(value));

    return this;
  }

  /**
   * Adds a long key/value pair to be included, via MDC, with the next log call.
   *
   * @param key the MDC key
   * @param value the MDC value
   * @return this logger, for chaining
   */
  public SemanticLogger with(String key, long value) {
    loggingContext.with(key, Long.toString(value));

    return this;
  }

  /**
   * Logs a message at debug level, with any accumulated {@link #with(String, String)} values
   * applied to the MDC for the duration of the call.
   *
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void debug(String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.debug(format, arguments);
        });
  }

  /**
   * Logs a message and throwable at debug level, with any accumulated {@link #with(String, String)}
   * values applied to the MDC for the duration of the call.
   *
   * @param throwable the throwable to log alongside the message
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void debug(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.debug(format, argumentsWithThrowable(arguments, throwable));
        });
  }

  /**
   * Logs a message at info level, with any accumulated {@link #with(String, String)} values applied
   * to the MDC for the duration of the call.
   *
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void info(String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.info(format, arguments);
        });
  }

  /**
   * Logs a message and throwable at info level, with any accumulated {@link #with(String, String)}
   * values applied to the MDC for the duration of the call.
   *
   * @param throwable the throwable to log alongside the message
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void info(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.info(format, argumentsWithThrowable(arguments, throwable));
        });
  }

  /**
   * Logs a message at warn level, with any accumulated {@link #with(String, String)} values applied
   * to the MDC for the duration of the call.
   *
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void warn(String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.warn(format, arguments);
        });
  }

  /**
   * Logs a message and throwable at warn level, with any accumulated {@link #with(String, String)}
   * values applied to the MDC for the duration of the call.
   *
   * @param throwable the throwable to log alongside the message
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void warn(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.warn(format, argumentsWithThrowable(arguments, throwable));
        });
  }

  /**
   * Logs a message at error level, with any accumulated {@link #with(String, String)} values
   * applied to the MDC for the duration of the call.
   *
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void error(String format, Object... arguments) {
    loggingContext.run(
        () -> {
          logger.error(format, arguments);
        });
  }

  /**
   * Logs a message and throwable at error level, with any accumulated {@link #with(String, String)}
   * values applied to the MDC for the duration of the call.
   *
   * @param throwable the throwable to log alongside the message
   * @param format an SLF4J-style message format string
   * @param arguments the format arguments
   */
  public void error(Throwable throwable, String format, Object... arguments) {
    loggingContext.run(
        () -> {
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
