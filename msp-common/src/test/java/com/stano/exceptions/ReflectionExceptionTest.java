package com.stano.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReflectionExceptionTest {

  @Test
  void shouldCreateExceptionWithMessageAndGetMessage() {
    assertThat(new ReflectionException("MESSAGE").getMessage()).isEqualTo("MESSAGE");
  }

  @Test
  void shouldCreateExceptionWithMessageAndNestedExceptionAndGetBoth() {
    var nestedException = new NullPointerException();
    var exception = new ReflectionException("MESSAGE", nestedException);

    assertThat(exception.getMessage()).isEqualTo("MESSAGE");
    assertThat(exception.getCause()).isEqualTo(nestedException);
  }
}
