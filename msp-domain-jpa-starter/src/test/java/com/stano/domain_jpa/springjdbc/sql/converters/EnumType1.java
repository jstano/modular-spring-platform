package com.stano.domain_jpa.springjdbc.sql.converters;

public enum EnumType1 {
   ONE("1"), TWO("2");

   public static EnumType1 fromCode(String code) {
      for (EnumType1 value : values()) {
         if (value.code.equalsIgnoreCase(code)) {
            return value;
         }
      }

      return null;
   }

   private final String code;

   EnumType1(String code) {
      this.code = code;
   }
}
