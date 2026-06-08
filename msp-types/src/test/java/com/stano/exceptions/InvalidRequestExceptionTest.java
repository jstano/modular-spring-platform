package com.stano.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidRequestExceptionTest {

    @Test
    void shouldCreateExceptionWithMessageAndGetMessage() {
        assertThat(new InvalidRequestException("MESSAGE").getMessage()).isEqualTo("MESSAGE");
    }

    @Test
    void shouldCreateExceptionWithMessageAndNestedExceptionAndGetBoth() {
        var nestedException = new NullPointerException();
        var exception = new InvalidRequestException("MESSAGE", nestedException);

        assertThat(exception.getMessage()).isEqualTo("MESSAGE");
        assertThat(exception.getCause()).isEqualTo(nestedException);
    }
}
