package com.stano.spring_boot_application.metrics;

import java.util.regex.Pattern;

public class Sanitizer {
  private static final String MASK = "******";

  // 🔑 Key-based patterns (expanded)
  private static final Pattern KEY_PATTERN =
      Pattern.compile(
          ".*(password|secret|token|key|credential|auth|session|cookie|jwt|signature|private|access|refresh).*",
          Pattern.CASE_INSENSITIVE);

  // 🔍 Value-based patterns
  private static final Pattern JWT_PATTERN =
      Pattern.compile("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$");

  private static final Pattern BEARER_PATTERN =
      Pattern.compile("^Bearer\\s+[A-Za-z0-9-_.]+$", Pattern.CASE_INSENSITIVE);

  private static final Pattern API_KEY_PATTERN =
      Pattern.compile("^(sk_live_|sk_test_)[A-Za-z0-9]+"); // Stripe-style example

  private static final Pattern BASE64_LIKE =
      Pattern.compile("^[A-Za-z0-9+/=]{20,}$"); // long encoded blobs

  public static Object sanitize(String key, Object value) {
    if (value == null) {
      return null;
    }

    String stringValue = value.toString();

    // 1. Key-based sanitization
    if (KEY_PATTERN.matcher(key).matches()) {
      return MASK;
    }

    // 2. Value-based sanitization
    if (JWT_PATTERN.matcher(stringValue).matches()
        || BEARER_PATTERN.matcher(stringValue).matches()
        || API_KEY_PATTERN.matcher(stringValue).matches()
        || BASE64_LIKE.matcher(stringValue).matches()) {
      return MASK;
    }

    // 3. High-entropy fallback (optional heuristic)
    if (stringValue.length() > 32
        && stringValue.matches(".*[A-Za-z].*")
        && stringValue.matches(".*\\d.*")) {
      return MASK;
    }

    return value;
  }
}
