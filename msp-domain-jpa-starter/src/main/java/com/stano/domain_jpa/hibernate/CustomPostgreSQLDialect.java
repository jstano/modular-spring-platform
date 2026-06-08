package com.stano.domain_jpa.hibernate;

import org.hibernate.dialect.PostgreSQLDialect;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {
  @Override
  public String getNativeIdentifierGeneratorStrategy() {
    return "identity";
  }
}
