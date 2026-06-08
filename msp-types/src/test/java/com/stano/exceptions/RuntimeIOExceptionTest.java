package com.stano.exceptions;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeIOExceptionTest {

    @Test
    void shouldCreateExceptionWithNestedIOExceptionAndGetIt() {
        var rootCause = new NullPointerException();
        var ioException = new IOException("Something bad happened", rootCause);
        var exception = new RuntimeIOException(ioException);

        assertThat(exception.getCause()).isEqualTo(ioException);
        assertThat(exception.getMessage()).isEqualTo("java.io.IOException: Something bad happened");
    }

    @Test
    void shouldCreateExceptionWithNestedOutOfMemoryErrorAndGetIt() {
        var rootCause = new OutOfMemoryError("Out of memory");
        var exception = new RuntimeIOException(rootCause);

        assertThat(exception.getCause()).isEqualTo(rootCause);
        assertThat(exception.getMessage()).isEqualTo("java.lang.OutOfMemoryError: Out of memory");
    }
}
