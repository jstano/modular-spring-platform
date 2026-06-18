package com.stano.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class RuntimeSQLExceptionTest {

  @Test
  void shouldCreateExceptionWithNestedSQLExceptionAndGetIt() {
    var rootCause = new NullPointerException();
    var sqlException = new SQLException("Something bad happened", rootCause);
    var exception = new RuntimeSQLException(sqlException);

    assertThat(exception.getCause()).isEqualTo(sqlException);
    assertThat(exception.getMessage()).isEqualTo("java.sql.SQLException: Something bad happened");
  }

  @Test
  void shouldCreateExceptionWithNestedNullPointerExceptionAndGetIt() {
    var rootCause = new NullPointerException("Null Pointer Error");
    var exception = new RuntimeSQLException(rootCause);

    assertThat(exception.getCause()).isEqualTo(rootCause);
    assertThat(exception.getMessage())
        .isEqualTo("java.lang.NullPointerException: Null Pointer Error");
  }

  @Test
  void shouldCreateExceptionWithNestedOutOfMemoryErrorAndGetIt() {
    var rootCause = new OutOfMemoryError("Out of memory");
    var exception = new RuntimeSQLException(rootCause);

    assertThat(exception.getCause()).isEqualTo(rootCause);
    assertThat(exception.getMessage()).isEqualTo("java.lang.OutOfMemoryError: Out of memory");
  }
}
