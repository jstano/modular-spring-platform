package com.stano.exceptions;

import java.io.IOException;

/**
 * Thrown to wrap a checked {@link IOException} as an unchecked exception.
 *
 * <p>Mapped to HTTP 500 (Internal Server Error) by {@code GlobalExceptionHandler}.
 */
public class RuntimeIOException extends RuntimeException {

  /**
   * Creates the exception wrapping the given {@link IOException}.
   *
   * @param cause the underlying I/O failure
   */
  public RuntimeIOException(IOException cause) {

    super(cause);
  }

  /**
   * Creates the exception wrapping the given cause.
   *
   * @param cause the underlying cause
   */
  public RuntimeIOException(Throwable cause) {

    super(cause);
  }
}
