package com.stano.data_source;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Class that holds a Connection object and delegates all calls to it. If the ownsConnection flag is
 * set then 1) The Connection will be closed when the close method is called. 2) All calls will be
 * forwarded to the Connection If it's not set then 1) The Connection will not be closed, and the
 * close method will do nothing. 2) Calls to various set methods that would modify the state of the
 * Connection, including basic transaction management will be skipped
 */
public class DelegatingConnection implements Connection {
  private final Connection connection;
  private final boolean ownsConnection;

  /**
   * Creates a non-owning delegating connection. State-mutating calls (commit, rollback, setters,
   * close, etc.) are silently skipped.
   *
   * @param connection the connection to delegate to
   */
  public DelegatingConnection(Connection connection) {
    this(connection, false);
  }

  /**
   * Creates a delegating connection.
   *
   * @param connection the connection to delegate to
   * @param ownsConnection {@code true} if this instance owns the connection and should forward
   *     state-mutating calls (including {@link #close()}) to it; {@code false} if those calls
   *     should be skipped, leaving the underlying connection unaffected
   */
  public DelegatingConnection(Connection connection, boolean ownsConnection) {
    this.connection = connection;
    this.ownsConnection = ownsConnection;
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return a new statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Statement createStatement() throws SQLException {
    return connection.createStatement();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @return a new callable statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    return connection.prepareCall(sql);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to convert
   * @return the native SQL as produced by the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public String nativeSQL(String sql) throws SQLException {
    return connection.nativeSQL(sql);
  }

  /**
   * Sets auto-commit mode on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param autoCommit the auto-commit setting
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    if (ownsConnection) {
      connection.setAutoCommit(autoCommit);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's auto-commit setting
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public boolean getAutoCommit() throws SQLException {
    return connection.getAutoCommit();
  }

  /**
   * Commits the underlying connection, if this instance owns the connection; otherwise does
   * nothing.
   *
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void commit() throws SQLException {
    if (ownsConnection) {
      connection.commit();
    }
  }

  /**
   * Rolls back the underlying connection, if this instance owns the connection; otherwise does
   * nothing.
   *
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void rollback() throws SQLException {
    if (ownsConnection) {
      connection.rollback();
    }
  }

  /**
   * Closes the underlying connection if this instance owns it and it is not already closed;
   * otherwise does nothing.
   *
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void close() throws SQLException {
    if (ownsConnection && !connection.isClosed()) {
      connection.close();
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return whether the underlying connection is closed
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public boolean isClosed() throws SQLException {
    return connection.isClosed();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's metadata
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return connection.getMetaData();
  }

  /**
   * Sets read-only mode on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param readOnly the read-only setting
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    if (ownsConnection) {
      connection.setReadOnly(readOnly);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's read-only setting
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public boolean isReadOnly() throws SQLException {
    return connection.isReadOnly();
  }

  /**
   * Sets the catalog on the underlying connection, if this instance owns the connection; otherwise
   * does nothing.
   *
   * @param catalog the catalog name
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setCatalog(String catalog) throws SQLException {
    if (ownsConnection) {
      connection.setCatalog(catalog);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's catalog
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public String getCatalog() throws SQLException {
    return connection.getCatalog();
  }

  /**
   * Sets the transaction isolation level on the underlying connection, if this instance owns the
   * connection; otherwise does nothing.
   *
   * @param level the transaction isolation level
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    if (ownsConnection) {
      connection.setTransactionIsolation(level);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's transaction isolation level
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public int getTransactionIsolation() throws SQLException {
    return connection.getTransactionIsolation();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's pending warnings
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public SQLWarning getWarnings() throws SQLException {
    return connection.getWarnings();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void clearWarnings() throws SQLException {
    connection.clearWarnings();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @return a new statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @return a new callable statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's type map
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return connection.getTypeMap();
  }

  /**
   * Sets the type map on the underlying connection, if this instance owns the connection; otherwise
   * does nothing.
   *
   * @param map the type map
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    if (ownsConnection) {
      connection.setTypeMap(map);
    }
  }

  /**
   * Sets the holdability on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param holdability the holdability setting
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setHoldability(int holdability) throws SQLException {
    if (ownsConnection) {
      connection.setHoldability(holdability);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's holdability
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public int getHoldability() throws SQLException {
    return connection.getHoldability();
  }

  /**
   * Creates a savepoint on the underlying connection, if this instance owns the connection.
   *
   * @return a new savepoint from the underlying connection
   * @throws SQLException if the underlying connection throws
   * @throws IllegalStateException if this instance does not own the connection
   */
  @Override
  public Savepoint setSavepoint() throws SQLException {
    if (ownsConnection) {
      return connection.setSavepoint();
    }

    throw new IllegalStateException("Savepoints not supported on externally-owned connections");
  }

  /**
   * Creates a named savepoint on the underlying connection, if this instance owns the connection.
   *
   * @param name the savepoint name
   * @return a new savepoint from the underlying connection
   * @throws SQLException if the underlying connection throws
   * @throws IllegalStateException if this instance does not own the connection
   */
  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    if (ownsConnection) {
      return connection.setSavepoint(name);
    }

    throw new IllegalStateException("Savepoints not supported on externally-owned connections");
  }

  /**
   * Rolls back to the given savepoint on the underlying connection, if this instance owns the
   * connection; otherwise does nothing.
   *
   * @param savepoint the savepoint to roll back to
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    if (ownsConnection) {
      connection.rollback(savepoint);
    }
  }

  /**
   * Releases the given savepoint on the underlying connection, if this instance owns the
   * connection; otherwise does nothing.
   *
   * @param savepoint the savepoint to release
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    if (ownsConnection) {
      connection.releaseSavepoint(savepoint);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @param resultSetHoldability the result set holdability
   * @return a new statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Statement createStatement(
      int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @param resultSetHoldability the result set holdability
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    return connection.prepareStatement(
        sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param resultSetType the result set type
   * @param resultSetConcurrency the result set concurrency
   * @param resultSetHoldability the result set holdability
   * @return a new callable statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public CallableStatement prepareCall(
      String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param autoGeneratedKeys whether generated keys should be returned
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return connection.prepareStatement(sql, autoGeneratedKeys);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param columnIndexes the indexes of columns whose generated keys should be returned
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    return connection.prepareStatement(sql, columnIndexes);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param sql the SQL to prepare
   * @param columnNames the names of columns whose generated keys should be returned
   * @return a new prepared statement from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    return connection.prepareStatement(sql, columnNames);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return a new CLOB from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Clob createClob() throws SQLException {
    return connection.createClob();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return a new BLOB from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Blob createBlob() throws SQLException {
    return connection.createBlob();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return a new NCLOB from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public NClob createNClob() throws SQLException {
    return connection.createNClob();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return a new SQLXML value from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public SQLXML createSQLXML() throws SQLException {
    return connection.createSQLXML();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param timeout the timeout in seconds
   * @return whether the underlying connection is valid
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public boolean isValid(int timeout) throws SQLException {
    return connection.isValid(timeout);
  }

  /**
   * Sets a client info property on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param name the property name
   * @param value the property value
   * @throws SQLClientInfoException if the underlying connection throws
   */
  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    if (ownsConnection) {
      connection.setClientInfo(name, value);
    }
  }

  /**
   * Sets client info properties on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param properties the properties to set
   * @throws SQLClientInfoException if the underlying connection throws
   */
  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    if (ownsConnection) {
      connection.setClientInfo(properties);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param name the property name
   * @return the underlying connection's client info property value
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public String getClientInfo(String name) throws SQLException {
    return connection.getClientInfo(name);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's client info properties
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Properties getClientInfo() throws SQLException {
    return connection.getClientInfo();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param typeName the SQL type name
   * @param elements the array elements
   * @return a new array from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return connection.createArrayOf(typeName, elements);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param typeName the SQL type name
   * @param attributes the struct attributes
   * @return a new struct from the underlying connection
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return connection.createStruct(typeName, attributes);
  }

  /**
   * Sets the schema on the underlying connection, if this instance owns the connection; otherwise
   * does nothing.
   *
   * @param schema the schema name
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setSchema(String schema) throws SQLException {
    if (ownsConnection) {
      connection.setSchema(schema);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's schema
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public String getSchema() throws SQLException {
    return connection.getSchema();
  }

  /**
   * Aborts the underlying connection, if this instance owns the connection; otherwise does nothing.
   *
   * @param executor the executor to run the abort task
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void abort(Executor executor) throws SQLException {
    if (ownsConnection) {
      connection.abort(executor);
    }
  }

  /**
   * Sets the network timeout on the underlying connection, if this instance owns the connection;
   * otherwise does nothing.
   *
   * @param executor the executor used to time out network operations
   * @param milliseconds the timeout in milliseconds
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    if (ownsConnection) {
      connection.setNetworkTimeout(executor, milliseconds);
    }
  }

  /**
   * Delegates to the underlying connection.
   *
   * @return the underlying connection's network timeout in milliseconds
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public int getNetworkTimeout() throws SQLException {
    return connection.getNetworkTimeout();
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param iface the requested interface
   * @param <T> the requested type
   * @return the underlying connection unwrapped as the requested type
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return connection.unwrap(iface);
  }

  /**
   * Delegates to the underlying connection.
   *
   * @param iface the requested interface
   * @return whether the underlying connection is a wrapper for the requested interface
   * @throws SQLException if the underlying connection throws
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return connection.isWrapperFor(iface);
  }
}
