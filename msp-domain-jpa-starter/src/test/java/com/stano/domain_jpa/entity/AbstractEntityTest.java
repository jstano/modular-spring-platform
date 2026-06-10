package com.stano.domain_jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AbstractEntityTest {
  @Test
  void idCanBeSetAndRetrieved() {
    TestEntity entity = new TestEntity("test");

    assertThat(entity.getId()).isNotNull();
  }

  @Test
  void versionStartsAtZero() {
    TestEntity entity = new TestEntity();

    assertThat(entity.getVersion()).isZero();
  }

  @Test
  void createdAtAndUpdatedAtAreRetrievable() {
    TestEntity entity = new TestEntity();

    assertThat(entity.getCreatedAt()).isNull();
    assertThat(entity.getUpdatedAt()).isNull();
  }
}
