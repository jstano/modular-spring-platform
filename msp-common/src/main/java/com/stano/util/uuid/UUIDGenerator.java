package com.stano.util.uuid;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

/** Static factory for generating {@link UUID} values, backed by the f4b6a3 uuid-creator library. */
public final class UUIDGenerator {
  /**
   * Generates a random (version 4) UUID.
   *
   * @return a new random UUID
   */
  public static UUID randomUUID() {
    return UuidCreator.getRandomBased();
  }

  /**
   * Generates a time-ordered (version 7) UUID whose values sort chronologically by creation time,
   * which makes it well suited as a database primary key.
   *
   * @return a new time-ordered UUID
   */
  public static UUID timeOrderedUUID() {
    return UuidCreator.getTimeOrderedEpoch();
  }

  private UUIDGenerator() {}
}
