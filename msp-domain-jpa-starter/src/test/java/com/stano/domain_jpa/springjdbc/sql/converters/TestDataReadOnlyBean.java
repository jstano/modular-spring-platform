package com.stano.domain_jpa.springjdbc.sql.converters;

public class TestDataReadOnlyBean {
   private final int id;
   private final String name;
   private final EnumType1 enumType1;
   private final EnumType2 enumType2;

   public TestDataReadOnlyBean(int id, String name, EnumType1 enumType1, EnumType2 enumType2) {
      this.id = id;
      this.name = name;
      this.enumType1 = enumType1;
      this.enumType2 = enumType2;
   }

   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public EnumType1 getEnumType1() {
      return enumType1;
   }

   public EnumType2 getEnumType2() {
      return enumType2;
   }
}
