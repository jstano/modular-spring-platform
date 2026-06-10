package com.stano.domain_jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class AbstractEntityIntegrationTest {
  @Autowired
  private TestEntityRepository repository;

  @Test
  void entityCanBePersisted() {
    TestEntity entity = new TestEntity("test");
    repository.add(entity);

    assertThat(entity.getId()).isNotNull();
  }

  @Test
  void savedEntityCanBeFoundById() {
    TestEntity entity = new TestEntity("test");
    repository.add(entity);

    assertThat(repository.findById(entity.getId())).isPresent();
  }

  @Test
  void versionIsZeroAfterInitialPersist() {
    TestEntity entity = new TestEntity("test");
    repository.add(entity);

    assertThat(entity.getVersion()).isZero();
  }
}
