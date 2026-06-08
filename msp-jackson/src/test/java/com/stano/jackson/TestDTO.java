package com.stano.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.OptionalInt;

public class TestDTO {
  private int id;
  private String name;
  private LocalDate localDate;
  private LocalDateTime localDateTime;
  private LocalTime localTime;
  private Integer optionalInt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public LocalTime getLocalTime() {
    return localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    this.localTime = localTime;
  }

  public OptionalInt getOptionalInt() {
    return optionalInt == null ? OptionalInt.empty() : OptionalInt.of(optionalInt);
  }

  public void setOptionalInt(OptionalInt optionalInt) {
    this.optionalInt = optionalInt.isPresent() ? optionalInt.getAsInt() : null;
  }
}
