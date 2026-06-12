package com.stano.domain_jpa.id;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class EntityIdGeneratorTest {
  @Test
  void generateReturnsNonNullUuid() {
    UUID id = EntityIdGenerator.generate();
    assertThat(id).isNotNull();
  }

  @Test
  void sequentialGenerationsProduceDistinctValues() {
    UUID id1 = EntityIdGenerator.generate();
    UUID id2 = EntityIdGenerator.generate();
    assertThat(id1).isNotEqualTo(id2);
  }

  @Test
  void uuidv7IdsAreTimeOrdered() {
    UUID id1 = EntityIdGenerator.generate();
    UUID id2 = EntityIdGenerator.generate();
    assertThat(id1.compareTo(id2)).isNegative();
  }
}
