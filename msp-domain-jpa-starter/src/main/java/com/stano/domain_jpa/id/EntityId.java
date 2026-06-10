package com.stano.domain_jpa.id;

import java.util.Objects;
import java.util.UUID;

public abstract class EntityId {
  private final UUID value;

  protected EntityId(UUID value) {
    this.value = Objects.requireNonNull(value);
  }

  public UUID value() {
    return value;
  }

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

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
