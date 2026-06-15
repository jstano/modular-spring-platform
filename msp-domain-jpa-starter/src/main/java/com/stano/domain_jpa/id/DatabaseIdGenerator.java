package com.stano.domain_jpa.id;

import com.stano.util.uuid.UUIDGenerator;
import java.util.UUID;

public final class DatabaseIdGenerator {
  public static UUID generate() {
    return UUIDGenerator.timeOrderedUUID();
  }

  private DatabaseIdGenerator() {}
}
