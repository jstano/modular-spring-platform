package com.stano.domain_jpa.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

@Entity
public class TestEntity {
  @Id
  @GeneratedValue
  private Long id;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
}
