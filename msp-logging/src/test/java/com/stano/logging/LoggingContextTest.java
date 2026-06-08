package com.stano.logging;

import org.apache.log4j.MDC;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingContextTest {

    @Test
    void theMdcShouldHaveTheValuesFromTheNewContextAfterSetupContextIsCalledAndBeforeRestoreContextIsCalled() {
        MDC.put("key1", "abc");
        MDC.put("key2", "def");
        MDC.put("key3", "ghi");

        var loggingContext = LoggingContext.newContext().with("key1", "ABC").with("key2", "XYZ");

        loggingContext.setupContext();

        try {
            assertThat(MDC.get("key1")).isEqualTo("ABC");
            assertThat(MDC.get("key2")).isEqualTo("XYZ");
            assertThat(MDC.get("key3")).isEqualTo("ghi");
        } finally {
            loggingContext.restoreContext();
        }
    }

    @Test
    void theMdcShouldHaveTheValuesFromThePriorContextRestoredAfterSetupContextAndRestoreContextAreCalled() {
        MDC.put("key1", "abc");
        MDC.put("key2", "def");
        MDC.put("key3", "ghi");

        var loggingContext = LoggingContext.newContext().with("key1", "ABC").with("key2", "XYZ").with("key4", "JHG");

        loggingContext.setupContext();
        loggingContext.restoreContext();

        assertThat(MDC.get("key1")).isEqualTo("abc");
        assertThat(MDC.get("key2")).isEqualTo("def");
        assertThat(MDC.get("key3")).isEqualTo("ghi");
        assertThat(MDC.get("key4")).isNull();
    }

    @Test
    void runShouldHaveTheContextSetToTheNewValuesAndRestoredAfterRunCompletes() {
        MDC.put("key1", "abc");
        MDC.put("key2", "def");
        MDC.put("key3", "ghi");

        var loggingContext = LoggingContext.newContext().with("key1", "ABC").with("key2", "XYZ");

        loggingContext.run(() -> {
            assertThat(MDC.get("key1")).isEqualTo("ABC");
            assertThat(MDC.get("key2")).isEqualTo("XYZ");
            assertThat(MDC.get("key3")).isEqualTo("ghi");
        });

        assertThat(MDC.get("key1")).isEqualTo("abc");
        assertThat(MDC.get("key2")).isEqualTo("def");
        assertThat(MDC.get("key3")).isEqualTo("ghi");
        assertThat(MDC.get("key4")).isNull();
    }

    @Test
    void ifRunEncountersAnExceptionTheContextShouldBeRestoredAfterRunCompletes() {
        MDC.put("key1", "abc");
        MDC.put("key2", "def");
        MDC.put("key3", "ghi");

        var loggingContext = LoggingContext.newContext().with("key1", "ABC").with("key2", "XYZ");

        try {
            loggingContext.run(() -> {
                throw new NullPointerException();
            });
        } catch (NullPointerException ignored) {
        }

        assertThat(MDC.get("key1")).isEqualTo("abc");
        assertThat(MDC.get("key2")).isEqualTo("def");
        assertThat(MDC.get("key3")).isEqualTo("ghi");
        assertThat(MDC.get("key4")).isNull();
    }
}
