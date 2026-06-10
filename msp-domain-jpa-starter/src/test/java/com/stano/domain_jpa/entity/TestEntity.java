package com.stano.domain_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.UUID;

@Entity
public class TestEntity extends AbstractEntity<TestEntityId> {
  private String name;
  private LocalDate birthDate;
  private LocalDateTime birthDateTime;
  private LocalTime birthTime;
  private DayOfWeek birthDayOfWeek;
  private Locale locale;
  private ZoneId zoneId;
  private URL url;
  @Column(precision = 19, scale = 4)
  private BigDecimal decimal;
  private boolean active;

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

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public LocalDateTime getBirthDateTime() {
    return birthDateTime;
  }

  public void setBirthDateTime(LocalDateTime birthDateTime) {
    this.birthDateTime = birthDateTime;
  }

  public LocalTime getBirthTime() {
    return birthTime;
  }

  public void setBirthTime(LocalTime birthTime) {
    this.birthTime = birthTime;
  }

  public DayOfWeek getBirthDayOfWeek() {
    return birthDayOfWeek;
  }

  public void setBirthDayOfWeek(DayOfWeek birthDayOfWeek) {
    this.birthDayOfWeek = birthDayOfWeek;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public ZoneId getZoneId() {
    return zoneId;
  }

  public void setZoneId(ZoneId zoneId) {
    this.zoneId = zoneId;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  public BigDecimal getDecimal() {
    return decimal;
  }

  public void setDecimal(BigDecimal decimal) {
    this.decimal = decimal;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  protected TestEntityId typedId(UUID value) {
    return new TestEntityId(value);
  }
}
