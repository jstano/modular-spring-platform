package com.stano.exceptions;

/**
 * Thrown when a request's contents are syntactically valid but semantically invalid, e.g. it fails
 * business-rule validation.
 *
 * <p>Mapped to HTTP 400 (Bad Request) by {@code GlobalExceptionHandler}, alongside {@link
 * BadRequestException}.
 */
public class InvalidRequestException extends RuntimeException {
  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public InvalidRequestException(String message) {
    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public InvalidRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
