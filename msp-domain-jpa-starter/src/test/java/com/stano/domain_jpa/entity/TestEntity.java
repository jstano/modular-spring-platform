package com.stano.domain_jpa.entity;

import jakarta.persistence.Entity;
import java.util.UUID;

@Entity
public class TestEntity extends AbstractEntity<TestEntityId> {
  private String name;

  public TestEntity() {}

  public TestEntity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  protected TestEntityId typedId(UUID value) {
    return new TestEntityId(value);
  }
}
