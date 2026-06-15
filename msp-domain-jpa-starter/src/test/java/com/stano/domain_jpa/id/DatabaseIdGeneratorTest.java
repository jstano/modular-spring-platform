package com.stano.domain_jpa.id;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DatabaseIdGeneratorTest {
  @Test
  void generateReturnsNonNullUuid() {
    UUID id = DatabaseIdGenerator.generate();
    assertThat(id).isNotNull();
  }

  @Test
  void sequentialGenerationsProduceDistinctValues() {
    UUID id1 = DatabaseIdGenerator.generate();
    UUID id2 = DatabaseIdGenerator.generate();
    assertThat(id1).isNotEqualTo(id2);
  }

  @Test
  void uuidv7IdsAreTimeOrdered() {
    UUID id1 = DatabaseIdGenerator.generate();
    UUID id2 = DatabaseIdGenerator.generate();
    assertThat(id1.compareTo(id2)).isNegative();
  }
}
