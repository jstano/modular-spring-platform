package com.stano.domain_jpa.hibernate.custom_types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;

public abstract class AbstractUserType<T> implements UserType<T>, EnhancedUserType<T> {
  protected final int sqlType;
  protected final Class<T> returnedClass;

  protected AbstractUserType(int sqlType, Class<T> returnedClass) {
    this.sqlType = sqlType;
    this.returnedClass = returnedClass;
  }

  @Override
  public int getSqlType() {
    return sqlType;
  }

  @Override
  public Class<T> returnedClass() {
    return returnedClass;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return x == y || !(x == null || y == null) && x.equals(y);
  }

  @Override
  public int hashCode(Object o) throws HibernateException {
    return o.hashCode();
  }

  @Override
  public T deepCopy(T o) throws HibernateException {
    return o;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(T object) throws HibernateException {
    return (Serializable)object;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T assemble(Serializable cached, Object owner) throws HibernateException {
    return (T)cached;
  }

  @Override
  public T replace(T original, T target, Object owner) throws HibernateException {
    return original;
  }
}
