package com.stano.jackson;

import java.time.LocalDate;

public class ImmutableValue {
  private final int id;
  private final String name;
  private final LocalDate birthDate;

  public ImmutableValue(int id, String name, LocalDate birthDate) {
    this.id = id;
    this.name = name;
    this.birthDate = birthDate;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }
}
