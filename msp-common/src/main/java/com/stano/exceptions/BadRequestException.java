package com.stano.exceptions;

/**
 * Thrown when a client request is malformed or fails basic validation.
 *
 * <p>Mapped to HTTP 400 (Bad Request) by {@code GlobalExceptionHandler}.
 */
public class BadRequestException extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public BadRequestException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public BadRequestException(String message, Throwable cause) {

    super(message, cause);
  }
}
