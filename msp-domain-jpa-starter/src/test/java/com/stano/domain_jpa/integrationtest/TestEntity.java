package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "IntegrationTestEntity")
@Table(name = "test")
public class TestEntity extends AbstractEntity<TestEntityId> {
  private String name;
  private LocalDate birthDate;

  public TestEntity() {}

  public TestEntity(String name, LocalDate birthDate) {
    this.name = name;
    this.birthDate = birthDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  @Override
  protected TestEntityId typedId(UUID value) {
    return new TestEntityId(value);
  }
}
