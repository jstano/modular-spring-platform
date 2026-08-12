package com.stano.logging;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;

/**
 * Wrapper around SLF4J's {@link MDC} that scopes a set of key/value pairs to the execution of a
 * block of code, restoring whatever MDC values (or absence of values) were present beforehand once
 * the block completes.
 *
 * <p>Typical usage: {@code LoggingContext.newContext().with("orderId", id).run(() -> doWork())}.
 */
public class LoggingContext {
  private final Map<String, String> data = new HashMap<>();
  private final Map<String, String> savedValues = new HashMap<>();

  /**
   * Creates a new, empty logging context.
   *
   * @return a new {@code LoggingContext}
   */
  public static LoggingContext newContext() {
    return new LoggingContext();
  }

  /**
   * Adds a key/value pair to be applied to the MDC when this context is run.
   *
   * @param key the MDC key
   * @param value the MDC value
   * @return this context, for chaining
   */
  public LoggingContext with(String key, String value) {
    data.put(key, value);

    return this;
  }

  /**
   * Applies this context's key/value pairs to the MDC, runs the given code, and then restores the
   * MDC to its prior state, even if the code throws.
   *
   * @param runnable the code to run with this context's values applied to the MDC
   */
  public void run(Runnable runnable) {
    setupContext();

    try {
      runnable.run();
    } finally {
      restoreContext();
    }
  }

  /**
   * Applies this context's key/value pairs to the MDC, first saving whatever value (if any) was
   * previously set for each key so it can be restored later by {@link #restoreContext()}.
   */
  public void setupContext() {
    data.forEach(
        (key, value) -> {
          savedValues.put(key, MDC.get(key));

          MDC.put(key, value);
        });
  }

  /**
   * Restores the MDC values that were present, per key, before {@link #setupContext()} was called,
   * removing keys that had no prior value.
   */
  public void restoreContext() {
    savedValues.forEach(
        (key, value) -> {
          if (value == null) {
            MDC.remove(key);
          } else {
            MDC.put(key, value);
          }
        });
  }
}
