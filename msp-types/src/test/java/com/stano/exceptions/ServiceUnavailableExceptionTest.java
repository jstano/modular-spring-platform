package com.stano.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServiceUnavailableExceptionTest {

  @Test
  void shouldCreateExceptionWithMessageAndGetMessage() {
    assertThat(new ServiceUnavailableException("MESSAGE").getMessage()).isEqualTo("MESSAGE");
  }

  @Test
  void shouldCreateExceptionWithMessageAndNestedExceptionAndGetBoth() {
    var nestedException = new NullPointerException();
    var exception = new ServiceUnavailableException("MESSAGE", nestedException);

    assertThat(exception.getMessage()).isEqualTo("MESSAGE");
    assertThat(exception.getCause()).isEqualTo(nestedException);
  }
}
