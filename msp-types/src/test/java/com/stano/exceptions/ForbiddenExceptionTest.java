package com.stano.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ForbiddenExceptionTest {

    @Test
    void shouldCreateExceptionWithMessageAndGetMessage() {
        assertThat(new ForbiddenException("MESSAGE").getMessage()).isEqualTo("MESSAGE");
    }

    @Test
    void shouldCreateExceptionWithMessageAndNestedExceptionAndGetBoth() {
        var nestedException = new NullPointerException();
        var exception = new ForbiddenException("MESSAGE", nestedException);

        assertThat(exception.getMessage()).isEqualTo("MESSAGE");
        assertThat(exception.getCause()).isEqualTo(nestedException);
    }
}
