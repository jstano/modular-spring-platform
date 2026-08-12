package com.stano.data_source.routable_datasource;

/**
 * Thread-local holder for the identifier of the "current" tenant database.
 *
 * <p>{@link RoutableDataSource} consults this holder to decide which underlying {@link
 * javax.sql.DataSource} to route JDBC/JPA calls to. Set the identifier before issuing routed calls,
 * and always clear it in a {@code finally} block (or use {@link DatabaseContextRunner} to avoid the
 * boilerplate). The value is held in an {@link InheritableThreadLocal}, so it propagates to threads
 * spawned from the thread that set it.
 */
public final class DatabaseContextHolder {
  private static final ThreadLocal<Long> contextHolder = new InheritableThreadLocal<>();

  /**
   * Returns the database identifier currently set for this thread.
   *
   * @return the current database identifier, or {@code null} if none is set
   */
  public static Long getDatabaseId() {
    return contextHolder.get();
  }

  /**
   * Sets the current database identifier for this thread.
   *
   * @param databaseId the identifier to route subsequent calls to
   * @return the previously set identifier, or {@code null} if none was set
   */
  public static Long setDatabaseId(Long databaseId) {
    Long currentValue = contextHolder.get();
    contextHolder.set(databaseId);
    return currentValue;
  }

  /** Clears the database identifier for this thread. */
  public static void clear() {
    contextHolder.remove();
  }

  private DatabaseContextHolder() {}
}
