package com.stano.exceptions;

/**
 * Thrown when a request lacks valid authentication credentials.
 *
 * <p>Mapped to HTTP 401 (Unauthorized) by {@code GlobalExceptionHandler}.
 */
public class UnauthorizedException extends RuntimeException {

  /** Creates the exception with no detail message. */
  public UnauthorizedException() {}

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public UnauthorizedException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public UnauthorizedException(String message, Throwable cause) {

    super(message, cause);
  }
}
