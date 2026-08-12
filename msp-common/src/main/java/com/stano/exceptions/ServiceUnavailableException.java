package com.stano.exceptions;

/**
 * Thrown when a dependent service or resource is temporarily unavailable and the request cannot be
 * completed.
 *
 * <p>Mapped to HTTP 503 (Service Unavailable) by {@code GlobalExceptionHandler}.
 */
public class ServiceUnavailableException extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ServiceUnavailableException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ServiceUnavailableException(String message, Throwable cause) {

    super(message, cause);
  }
}
