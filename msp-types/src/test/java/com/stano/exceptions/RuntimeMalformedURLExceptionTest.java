package com.stano.exceptions;

import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeMalformedURLExceptionTest {

    @Test
    void shouldCreateExceptionWithNestedMalformedURLExceptionAndGetIt() {
        var malformedURLException = new MalformedURLException("URL is bad");
        var exception = new RuntimeMalformedURLException(malformedURLException);

        assertThat(exception.getCause()).isEqualTo(malformedURLException);
        assertThat(exception.getMessage()).isEqualTo("java.net.MalformedURLException: URL is bad");
    }
}
