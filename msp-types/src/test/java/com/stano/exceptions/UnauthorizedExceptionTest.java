package com.stano.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest {

  @Test
  void shouldCreateExceptionWithDefaultConstructor() {
    assertThat(new UnauthorizedException().getMessage()).isNull();
  }

  @Test
  void shouldCreateExceptionWithMessageAndGetMessage() {
    assertThat(new UnauthorizedException("MESSAGE").getMessage()).isEqualTo("MESSAGE");
  }

  @Test
  void shouldCreateExceptionWithMessageAndNestedExceptionAndGetBoth() {
    var nestedException = new NullPointerException();
    var exception = new UnauthorizedException("MESSAGE", nestedException);

    assertThat(exception.getMessage()).isEqualTo("MESSAGE");
    assertThat(exception.getCause()).isEqualTo(nestedException);
  }
}
