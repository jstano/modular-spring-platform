package com.stano.domain_jpa.entity;

import jakarta.persistence.Entity;
import java.util.UUID;

@Entity
class AnotherTestEntity extends AbstractEntity<TestEntityId> {
  @Override
  protected TestEntityId typedId(UUID value) {
    return new TestEntityId(value);
  }
}
