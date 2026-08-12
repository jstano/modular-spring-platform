package com.stano.exceptions;

import java.sql.SQLException;

/**
 * Thrown to wrap a checked {@link SQLException} as an unchecked exception.
 *
 * <p>Mapped to HTTP 500 (Internal Server Error) by {@code GlobalExceptionHandler}.
 */
public class RuntimeSQLException extends RuntimeException {

  /**
   * Creates the exception wrapping the given {@link SQLException}.
   *
   * @param cause the underlying SQL failure
   */
  public RuntimeSQLException(SQLException cause) {

    super(cause);
  }

  /**
   * Creates the exception wrapping the given cause.
   *
   * @param cause the underlying cause
   */
  public RuntimeSQLException(Throwable cause) {

    super(cause);
  }
}
