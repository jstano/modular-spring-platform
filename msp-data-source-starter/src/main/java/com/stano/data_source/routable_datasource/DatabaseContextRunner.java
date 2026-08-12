package com.stano.data_source.routable_datasource;

import java.util.function.Supplier;

/**
 * Convenience wrapper around {@link DatabaseContextHolder} that sets the current database
 * identifier for the duration of a supplied operation and always restores the previous value
 * afterward, even if the operation throws.
 */
public final class DatabaseContextRunner {
  /**
   * Runs the given supplier with the current thread's database context set to the given identifier,
   * restoring the previous identifier afterward.
   *
   * @param databaseId the database identifier to route calls to while the supplier runs
   * @param supplier the operation to run
   * @param <T> the type of value produced by the operation
   * @return the value produced by the supplier
   */
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
