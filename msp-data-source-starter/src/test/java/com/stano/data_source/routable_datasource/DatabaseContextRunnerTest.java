package com.stano.data_source.routable_datasource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DatabaseContextRunnerTest {

  @Test
  void databaseContextRunnerShouldSetAndCleanupContextProperly() {
    DatabaseContextHolder.setDatabaseId(123L);

    Long result =
        DatabaseContextRunner.runWithDatabase(456L, () -> DatabaseContextHolder.getDatabaseId());

    assertThat(result).isEqualTo(456L);
    assertThat(DatabaseContextHolder.getDatabaseId()).isEqualTo(123L);
  }
}
