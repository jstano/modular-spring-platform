package com.stano.data_source.routable_datasource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseContextHolderTest {
  private Long saveDatabaseID;

  @BeforeEach
  void setup() {
    saveDatabaseID = DatabaseContextHolder.getDatabaseId();
  }

  @AfterEach
  void cleanup() {
    if (saveDatabaseID != null) {
      DatabaseContextHolder.setDatabaseId(saveDatabaseID);
    } else {
      DatabaseContextHolder.clear();
    }
  }

  @Test
  void shouldBeAbleToGetWhatWasSet() {
    DatabaseContextHolder.setDatabaseId(123L);

    assertThat(DatabaseContextHolder.getDatabaseId()).isEqualTo(123L);
  }

  @Test
  void setShouldReturnThePriorValue() {
    DatabaseContextHolder.setDatabaseId(123L);

    Long priorValue = DatabaseContextHolder.setDatabaseId(456L);

    assertThat(priorValue).isEqualTo(123L);
  }

  @Test
  void afterCallingClearGetShouldReturnNull() {
    DatabaseContextHolder.setDatabaseId(123L);

    DatabaseContextHolder.clear();

    assertThat(DatabaseContextHolder.getDatabaseId()).isNull();
  }
}
