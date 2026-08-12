package com.stano.exceptions;

/**
 * Thrown when an authenticated caller is not permitted to perform the requested operation.
 *
 * <p>Mapped to HTTP 403 (Forbidden) by {@code GlobalExceptionHandler}.
 */
public class ForbiddenException extends RuntimeException {

  /** Creates the exception with no detail message. */
  public ForbiddenException() {}

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ForbiddenException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ForbiddenException(String message, Throwable cause) {

    super(message, cause);
  }
}
