package com.stano.domain_jpa.routable_datasource;

import java.util.function.Supplier;

public final class DatabaseContextRunner {
  public static <T> T runWithDatabase(long databaseId, Supplier<T> supplier) {
    Long oldDatabaseID = DatabaseContextHolder.getDatabaseId();

    try {
      DatabaseContextHolder.setDatabaseId(databaseId);

      return supplier.get();
    } finally {
      DatabaseContextHolder.setDatabaseId(oldDatabaseID);
    }
  }

  private DatabaseContextRunner() {}
}
