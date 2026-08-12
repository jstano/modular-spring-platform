package com.stano.domain_jpa.id;

import com.stano.util.uuid.UUIDGenerator;
import java.util.UUID;

/** Generates UUID values used as {@link DatabaseId} identifiers. */
public final class DatabaseIdGenerator {
  /**
   * Generates a new, time-ordered UUID suitable for use as a {@link DatabaseId} value.
   *
   * @return a newly generated, time-ordered UUID
   */
  public static UUID generate() {
    return UUIDGenerator.timeOrderedUUID();
  }

  private DatabaseIdGenerator() {}
}
