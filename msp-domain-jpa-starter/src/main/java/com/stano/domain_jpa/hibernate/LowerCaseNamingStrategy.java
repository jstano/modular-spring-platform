package com.stano.domain_jpa.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class LowerCaseNamingStrategy implements PhysicalNamingStrategy {
  @Override
  public Identifier toPhysicalCatalogName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return apply(logicalName, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return apply(logicalName, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return apply(logicalName, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return apply(logicalName, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return apply(logicalName, jdbcEnvironment);
  }

  private Identifier apply(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
    if (name == null) {
      return null;
    }

    return new Identifier(name.getText().toLowerCase(), name.isQuoted());
  }
}
