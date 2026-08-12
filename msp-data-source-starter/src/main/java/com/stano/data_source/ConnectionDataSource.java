package com.stano.data_source;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.io.output.NullWriter;

/**
 * DataSource implementation that holds a Connection object. This is useful when a DataSource is
 * needed, i.e. in the JdbcTemplate constructor but when you don't want to use a real DataSource,
 * but simply wrap a DataSource around a Connection object.
 */
public class ConnectionDataSource implements DataSource, AutoCloseable {
  private final Connection connection;

  private PrintWriter logWriter = new PrintWriter(NullWriter.INSTANCE);

  /**
   * Creates a data source that always returns the given connection.
   *
   * @param connection the connection to wrap
   */
  public ConnectionDataSource(Connection connection) {
    this.connection = connection;
  }

  /**
   * Returns the wrapped connection.
   *
   * @return the wrapped connection
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public Connection getConnection() throws SQLException {
    return connection;
  }

  /**
   * Returns the wrapped connection, ignoring the supplied credentials.
   *
   * @param username ignored
   * @param password ignored
   * @return the wrapped connection
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return connection;
  }

  /**
   * Unsupported; always throws.
   *
   * @param iface the requested interface
   * @param <T> the requested type
   * @return never returns normally
   * @throws SQLException always thrown, since unwrapping is not supported
   */
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException("unwrap not supported");
  }

  /**
   * Always reports that this data source is not a wrapper for any interface.
   *
   * @param iface the requested interface
   * @return {@code false} always
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }

  /**
   * Returns the configured log writer.
   *
   * @return the log writer, defaulting to a no-op writer
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return logWriter;
  }

  /**
   * Sets the log writer.
   *
   * @param out the log writer to use
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    this.logWriter = out;
  }

  /**
   * Returns the login timeout in seconds.
   *
   * @return {@code 0} always, since login timeout is not applicable to a wrapped connection
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  /**
   * No-op; login timeout is not applicable to a wrapped connection.
   *
   * @param seconds ignored
   * @throws SQLException never thrown by this implementation
   */
  @Override
  public void setLoginTimeout(int seconds) throws SQLException {}

  /**
   * Unsupported; always throws.
   *
   * @return never returns normally
   * @throws SQLFeatureNotSupportedException always, since a parent logger is not supported
   */
  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException("getParentLogger not supported");
  }

  /**
   * Closes the wrapped connection.
   *
   * @throws SQLException if closing the underlying connection fails
   */
  @Override
  public void close() throws SQLException {
    connection.close();
  }
}
