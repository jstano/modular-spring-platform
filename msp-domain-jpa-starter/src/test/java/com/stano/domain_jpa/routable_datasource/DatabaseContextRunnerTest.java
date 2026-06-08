package com.stano.domain_jpa.routable_datasource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseContextRunnerTest {

    @Test
    void databaseContextRunnerShouldSetAndCleanupContextProperly() {
        DatabaseContextHolder.setDatabaseId(123L);

        Long result = DatabaseContextRunner.runWithDatabase(456L, () -> DatabaseContextHolder.getDatabaseId());

        assertThat(result).isEqualTo(456L);
        assertThat(DatabaseContextHolder.getDatabaseId()).isEqualTo(123L);
    }
}
