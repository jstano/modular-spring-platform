package com.stano.domain_jpa.entity;

import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.password.Password;
import com.stano.crypto.text.EncryptedText;
import com.stano.domain_jpa.jpa.converters.EncryptedStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Currency;
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
  private EncryptedBytes encryptedBytes;
  private EncryptedText encryptedText;
  private Password password;

  @Convert(converter = EncryptedStringConverter.class)
  private String encryptedString;

  private Currency currency;

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

  public EncryptedBytes getEncryptedBytes() {
    return encryptedBytes;
  }

  public void setEncryptedBytes(EncryptedBytes encryptedBytes) {
    this.encryptedBytes = encryptedBytes;
  }

  public EncryptedText getEncryptedText() {
    return encryptedText;
  }

  public void setEncryptedText(EncryptedText encryptedText) {
    this.encryptedText = encryptedText;
  }

  public Password getPassword() {
    return password;
  }

  public void setPassword(Password password) {
    this.password = password;
  }

  public String getEncryptedString() {
    return encryptedString;
  }

  public void setEncryptedString(String encryptedString) {
    this.encryptedString = encryptedString;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  protected TestEntityId typedId(UUID value) {
    return new TestEntityId(value);
  }
}
