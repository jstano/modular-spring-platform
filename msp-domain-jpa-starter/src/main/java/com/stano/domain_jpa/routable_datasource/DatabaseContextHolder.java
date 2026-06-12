package com.stano.domain_jpa.routable_datasource;

public final class DatabaseContextHolder {
  private static final ThreadLocal<Long> contextHolder = new InheritableThreadLocal<>();

  public static Long getDatabaseId() {
    return contextHolder.get();
  }

  public static Long setDatabaseId(Long databaseId) {
    Long currentValue = contextHolder.get();
    contextHolder.set(databaseId);
    return currentValue;
  }

  public static void clear() {
    contextHolder.remove();
  }

  private DatabaseContextHolder() {}
}
