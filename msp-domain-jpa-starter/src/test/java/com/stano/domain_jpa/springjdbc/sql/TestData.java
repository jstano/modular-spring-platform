package com.stano.domain_jpa.springjdbc.sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestData {
   private int testPk;
   private int intValue;
   private long longValue;
   private BigDecimal decimalValue;
   private String stringValue;
   private LocalDate dateValue;
   private LocalDateTime dateTimeValue;
   private LocalTime timeValue;
   private boolean bitBoolean;
   private boolean nativeBoolean;
   private boolean ynBoolean;
   private boolean yesNoBoolean;
   private Size size;
   private SizeWithCode sizeWithCode;
   private byte[] byteData;

   public int getTestPk() {
      return testPk;
   }

   public void setTestPk(int testPk) {
      this.testPk = testPk;
   }

   public int getIntValue() {
      return intValue;
   }

   public void setIntValue(int intValue) {
      this.intValue = intValue;
   }

   public long getLongValue() {
      return longValue;
   }

   public void setLongValue(long longValue) {
      this.longValue = longValue;
   }

   public BigDecimal getDecimalValue() {
      return decimalValue;
   }

   public void setDecimalValue(BigDecimal decimalValue) {
      this.decimalValue = decimalValue;
   }

   public String getStringValue() {
      return stringValue;
   }

   public void setStringValue(String stringValue) {
      this.stringValue = stringValue;
   }

   public LocalDate getDateValue() {
      return dateValue;
   }

   public void setDateValue(LocalDate dateValue) {
      this.dateValue = dateValue;
   }

   public LocalDateTime getDateTimeValue() {
      return dateTimeValue;
   }

   public void setDateTimeValue(LocalDateTime dateTimeValue) {
      this.dateTimeValue = dateTimeValue;
   }

   public LocalTime getTimeValue() {
      return timeValue;
   }

   public void setTimeValue(LocalTime timeValue) {
      this.timeValue = timeValue;
   }

   public boolean isBitBoolean() {
      return bitBoolean;
   }

   public void setBitBoolean(boolean bitBoolean) {
      this.bitBoolean = bitBoolean;
   }

   public boolean isNativeBoolean() {
      return nativeBoolean;
   }

   public void setNativeBoolean(boolean nativeBoolean) {
      this.nativeBoolean = nativeBoolean;
   }

   public boolean isYnBoolean() {
      return ynBoolean;
   }

   public void setYnBoolean(boolean ynBoolean) {
      this.ynBoolean = ynBoolean;
   }

   public boolean isYesNoBoolean() {
      return yesNoBoolean;
   }

   public void setYesNoBoolean(boolean yesNoBoolean) {
      this.yesNoBoolean = yesNoBoolean;
   }

   public Size getSize() {
      return size;
   }

   public void setSize(Size size) {
      this.size = size;
   }

   public SizeWithCode getSizeWithCode() {
      return sizeWithCode;
   }

   public void setSizeWithCode(SizeWithCode sizeWithCode) {
      this.sizeWithCode = sizeWithCode;
   }

   public byte[] getByteData() {
      return byteData;
   }

   public void setByteData(byte[] byteData) {
      this.byteData = byteData;
   }
}
