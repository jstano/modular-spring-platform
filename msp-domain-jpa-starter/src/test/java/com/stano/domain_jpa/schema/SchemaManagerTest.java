package com.stano.domain_jpa.schema;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stano.schema.installer.schemacontext.SchemaContext;
import com.stano.schema.model.Version;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

class SchemaManagerTest {
  private final SchemaManager schemaManager = new SchemaManager();

  @Test
  void skipsWhenSchemaIsInstalledAndNotVersionBased() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(false);

    schemaManager.installOrMigrate(dataSource, schemaContext, false);

    verify(schemaContext).schemaIsInstalled(connection);
    verify(schemaContext).isVersionBased();
    verify(schemaContext, never()).getDatabaseVersion(any());
  }

  @Test
  void skipsWhenDatabaseVersionIsNull() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(true);
    when(schemaContext.getDatabaseVersion(connection)).thenReturn(null);

    schemaManager.installOrMigrate(dataSource, schemaContext, false);

    verify(schemaContext).getDatabaseVersion(connection);
    verify(schemaContext, never()).getMigrationScriptLocator(any());
  }

  @Test
  void skipsWhenDatabaseVersionIsGreaterOrEqual() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(true);
    when(schemaContext.getDatabaseVersion(connection)).thenReturn(new Version(2, 0));
    when(schemaContext.getSchemaVersion()).thenReturn(new Version(1, 0));

    schemaManager.installOrMigrate(dataSource, schemaContext, false);

    verify(schemaContext, never()).getMigrationScriptLocator(any());
  }

  @Test
  void skipsWhenMigrationLocatorIsNull() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(true);
    when(schemaContext.getDatabaseVersion(connection)).thenReturn(new Version(1, 0));
    when(schemaContext.getSchemaVersion()).thenReturn(new Version(2, 0));
    when(schemaContext.getMigrationScriptLocator(connection)).thenReturn(null);

    schemaManager.installOrMigrate(dataSource, schemaContext, false);

    verify(schemaContext).getMigrationScriptLocator(connection);
  }

  @Test
  void acceptsAutoMigrateTrueParameter() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(false);

    schemaManager.installOrMigrate(dataSource, schemaContext, true);

    verify(schemaContext).schemaIsInstalled(connection);
  }

  @Test
  void acceptsAutoMigrateFalseParameter() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    SchemaContext schemaContext = mock(SchemaContext.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(schemaContext.schemaIsInstalled(connection)).thenReturn(true);
    when(schemaContext.isVersionBased()).thenReturn(false);

    schemaManager.installOrMigrate(dataSource, schemaContext, false);

    verify(schemaContext).schemaIsInstalled(connection);
  }

  @Test
  void wrapsConnectionSqlExceptionInIllegalStateException() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    SQLException sqlException = new SQLException("connection failed");

    when(dataSource.getConnection()).thenThrow(sqlException);

    SchemaContext schemaContext = mock(SchemaContext.class);

    assertThatThrownBy(() -> schemaManager.installOrMigrate(dataSource, schemaContext, false))
        .isInstanceOf(IllegalStateException.class)
        .hasCause(sqlException);
  }
}
