package com.stano.exceptions;

/**
 * Thrown when a request conflicts with the current state of the target resource, e.g. a duplicate
 * creation or a concurrent modification conflict.
 *
 * <p>Mapped to HTTP 409 (Conflict) by {@code GlobalExceptionHandler}.
 */
public class ResourceConflictException extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ResourceConflictException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ResourceConflictException(String message, Throwable cause) {

    super(message, cause);
  }
}
