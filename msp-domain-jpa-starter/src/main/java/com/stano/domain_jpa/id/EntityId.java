package com.stano.domain_jpa.id;

import java.util.Objects;
import java.util.UUID;

/**
 * Base class for entity-specific, UUID-backed identifier types.
 *
 * <p>Each {@link com.stano.domain_jpa.entity.AbstractEntity} subtype defines its own {@code
 * EntityId} subclass (e.g. {@code CustomerId extends EntityId}) so that ids are not interchangeable
 * between unrelated entity types at compile time. Pair each subtype with an {@link
 * EntityIdAttributeConverter} implementation to persist it.
 */
public abstract class EntityId {
  private final UUID value;

  /**
   * Creates an id wrapping the given non-null UUID value.
   *
   * @param value the underlying UUID value, must not be {@code null}
   * @throws NullPointerException if {@code value} is {@code null}
   */
  protected EntityId(UUID value) {
    this.value = Objects.requireNonNull(value);
  }

  /**
   * Returns the underlying UUID value.
   *
   * @return the identifier's raw UUID value
   */
  public UUID value() {
    return value;
  }

  /**
   * Compares this id to another for equality based on the underlying UUID value and runtime type.
   *
   * @param o the object to compare against
   * @return {@code true} if {@code o} is an {@code EntityId} with the same value
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EntityId other)) {
      return false;
    }
    return value.equals(other.value);
  }

  /**
   * Returns a hash code consistent with {@link #equals}, based on the underlying UUID value.
   *
   * @return the hash code for this id
   */
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Returns the string form of the underlying UUID value.
   *
   * @return the UUID value's string representation
   */
  @Override
  public String toString() {
    return value.toString();
  }
}
