package com.stano.domain_jpa.springjdbc.sql;

public enum SizeWithCode {
   SMALL("S"),
   MEDIUM("M"),
   LARGE("L");

   private final String code;

   public String getCode() {
      return code;
   }

   SizeWithCode(String code) {
      this.code = code;
   }

   public static SizeWithCode fromCode(String code) {
      for (SizeWithCode value : values()) {
         if (value.getCode().equalsIgnoreCase(code)) {
            return value;
         }
      }

      return null;
   }
}
