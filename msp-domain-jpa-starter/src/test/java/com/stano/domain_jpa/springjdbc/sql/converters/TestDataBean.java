package com.stano.domain_jpa.springjdbc.sql.converters;

public class TestDataBean {
   private int id;
   private String name;
   private EnumType1 enumType1;
   private EnumType2 enumType2;

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

   public EnumType1 getEnumType1() {
      return enumType1;
   }

   public void setEnumType1(EnumType1 enumType1) {
      this.enumType1 = enumType1;
   }

   public EnumType2 getEnumType2() {
      return enumType2;
   }

   public void setEnumType2(EnumType2 enumType2) {
      this.enumType2 = enumType2;
   }
}
