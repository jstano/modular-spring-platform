package com.stano.exceptions;

/**
 * Thrown to signal an unexpected server-side failure that does not fit a more specific exception
 * type.
 *
 * <p>Mapped to HTTP 500 (Internal Server Error) by {@code GlobalExceptionHandler}.
 */
public class InternalServerError extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public InternalServerError(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public InternalServerError(String message, Throwable cause) {

    super(message, cause);
  }
}
