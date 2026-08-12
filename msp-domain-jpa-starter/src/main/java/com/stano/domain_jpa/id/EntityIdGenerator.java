package com.stano.domain_jpa.id;

import com.stano.util.uuid.UUIDGenerator;
import java.util.UUID;

/** Generates UUID values used as {@link EntityId} identifiers. */
public final class EntityIdGenerator {
  /**
   * Generates a new, time-ordered UUID suitable for use as an {@link EntityId} value.
   *
   * @return a newly generated, time-ordered UUID
   */
  public static UUID generate() {
    return UUIDGenerator.timeOrderedUUID();
  }

  private EntityIdGenerator() {}
}
