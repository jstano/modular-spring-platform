package com.stano.exceptions;

/**
 * Thrown to wrap a checked {@link java.net.MalformedURLException} as an unchecked exception.
 *
 * <p>Mapped to HTTP 500 (Internal Server Error) by {@code GlobalExceptionHandler}.
 */
public class RuntimeMalformedURLException extends RuntimeException {
  /**
   * Creates the exception wrapping the given cause.
   *
   * @param cause the underlying cause
   */
  public RuntimeMalformedURLException(Throwable cause) {
    super(cause);
  }
}
