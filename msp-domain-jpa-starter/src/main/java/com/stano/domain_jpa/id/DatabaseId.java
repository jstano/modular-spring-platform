package com.stano.domain_jpa.id;

import java.util.UUID;

/**
 * A generic, UUID-backed database identifier.
 *
 * <p>Unlike {@link EntityId}, which is subclassed once per entity type and paired with a dedicated
 * {@link EntityIdAttributeConverter}, {@code DatabaseId} is a single type persisted via the
 * auto-applied {@link DatabaseIdAttributeConverter}. Use it where a typed, per-entity identifier
 * class is unnecessary.
 */
public interface DatabaseId {
  /**
   * Returns the underlying UUID value.
   *
   * @return the identifier's raw UUID value
   */
  UUID value();
}
