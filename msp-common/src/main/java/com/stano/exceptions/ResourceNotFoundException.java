package com.stano.exceptions;

/**
 * Thrown when a requested resource does not exist.
 *
 * <p>Mapped to HTTP 404 (Not Found) by {@code GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ResourceNotFoundException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ResourceNotFoundException(String message, Throwable cause) {

    super(message, cause);
  }
}
