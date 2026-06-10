package com.stano.util.uuid;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

public final class UUIDGenerator {
  public static UUID randomUUID() {
    return UuidCreator.getRandomBased();
  }

  public static UUID timeOrderedUUID() {
    return UuidCreator.getTimeOrderedEpoch();
  }

  private UUIDGenerator() {
  }
}
