package com.stano.logging;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class LoggingContext {
  private final Map<String, String> data = new HashMap<>();
  private final Map<String, String> savedValues = new HashMap<>();

  public static LoggingContext newContext() {
    return new LoggingContext();
  }

  public LoggingContext with(String key, String value) {
    data.put(key, value);

    return this;
  }

  public void run(Runnable runnable) {
    setupContext();

    try {
      runnable.run();
    }
    finally {
      restoreContext();
    }
  }

  public void setupContext() {
    data.forEach((key, value) -> {
      savedValues.put(key, MDC.get(key));

      MDC.put(key, value);
    });
  }

  public void restoreContext() {
    savedValues.forEach((key, value) -> {
      if (value == null) {
        MDC.remove(key);
      }
      else {
        MDC.put(key, value);
      }
    });
  }
}
