package com.stano.exceptions;

/**
 * Thrown to wrap checked reflection failures (e.g. {@code IllegalAccessException}, {@code
 * InvocationTargetException}) as an unchecked exception.
 *
 * <p>Mapped to HTTP 500 (Internal Server Error) by {@code GlobalExceptionHandler}.
 */
public class ReflectionException extends RuntimeException {

  /** Creates the exception with no detail message. */
  public ReflectionException() {}

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ReflectionException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ReflectionException(String message, Throwable cause) {

    super(message, cause);
  }

  /**
   * Creates the exception wrapping the given cause.
   *
   * @param cause the underlying cause
   */
  public ReflectionException(Throwable cause) {

    super(cause);
  }
}
