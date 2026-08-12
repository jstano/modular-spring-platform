package com.stano.exceptions;

/**
 * Thrown when the target resource is locked and cannot currently be accessed or modified.
 *
 * <p>Mapped to HTTP 423 (Locked) by {@code GlobalExceptionHandler}.
 */
public class ResourceLockedException extends RuntimeException {

  /**
   * Creates the exception with a detail message.
   *
   * @param message the detail message
   */
  public ResourceLockedException(String message) {

    super(message);
  }

  /**
   * Creates the exception with a detail message and cause.
   *
   * @param message the detail message
   * @param cause the underlying cause
   */
  public ResourceLockedException(String message, Throwable cause) {

    super(message, cause);
  }
}
