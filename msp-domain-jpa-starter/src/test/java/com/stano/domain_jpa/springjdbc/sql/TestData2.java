package com.stano.domain_jpa.springjdbc.sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestData2 {
   private final int testPk;
   private final int intValue;
   private final long longValue;
   private final BigDecimal decimalValue;
   private final String stringValue;
   private final LocalDate dateValue;
   private final LocalDateTime dateTimeValue;
   private final LocalTime timeValue;
   private final boolean bitBoolean;
   private final boolean nativeBoolean;
   private final boolean ynBoolean;
   private final boolean yesNoBoolean;
   private final Size size;
   private final SizeWithCode sizeWithCode;
   private final byte[] byteData;

   public TestData2(int testPk,
                    int intValue,
                    long longValue,
                    BigDecimal decimalValue,
                    String stringValue,
                    LocalDate dateValue,
                    LocalDateTime dateTimeValue,
                    LocalTime timeValue,
                    boolean bitBoolean,
                    boolean nativeBoolean,
                    boolean ynBoolean,
                    boolean yesNoBoolean,
                    Size size,
                    SizeWithCode sizeWithCode,
                    byte[] byteData) {

      this.testPk = testPk;
      this.intValue = intValue;
      this.longValue = longValue;
      this.decimalValue = decimalValue;
      this.stringValue = stringValue;
      this.dateValue = dateValue;
      this.dateTimeValue = dateTimeValue;
      this.timeValue = timeValue;
      this.bitBoolean = bitBoolean;
      this.nativeBoolean = nativeBoolean;
      this.ynBoolean = ynBoolean;
      this.yesNoBoolean = yesNoBoolean;
      this.size = size;
      this.sizeWithCode = sizeWithCode;
      this.byteData = byteData;
   }

   public int getTestPk() {
      return testPk;
   }

   public int getIntValue() {
      return intValue;
   }

   public long getLongValue() {
      return longValue;
   }

   public BigDecimal getDecimalValue() {
      return decimalValue;
   }

   public String getStringValue() {
      return stringValue;
   }

   public LocalDate getDateValue() {
      return dateValue;
   }

   public LocalDateTime getDateTimeValue() {
      return dateTimeValue;
   }

   public LocalTime getTimeValue() {
      return timeValue;
   }

   public boolean isBitBoolean() {
      return bitBoolean;
   }

   public boolean isNativeBoolean() {
      return nativeBoolean;
   }

   public boolean isYnBoolean() {
      return ynBoolean;
   }

   public boolean isYesNoBoolean() {
      return yesNoBoolean;
   }

   public Size getSize() {
      return size;
   }

   public SizeWithCode getSizeWithCode() {
      return sizeWithCode;
   }

   public byte[] getByteData() {
      return byteData;
   }
}
