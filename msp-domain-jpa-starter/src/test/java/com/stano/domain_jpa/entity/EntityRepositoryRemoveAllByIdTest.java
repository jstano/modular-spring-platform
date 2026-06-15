package com.stano.domain_jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Regression test for a Spring Data JPA 4 query-derivation bug.
 *
 * <p>Spring Data JPA 4 validates every abstract method on a concrete repository interface at
 * startup by attempting to derive a query from the method name. For {@code
 * removeAllById(Iterable<TestEntityId> ids)}, it resolves the parameter as {@code
 * Iterable<TestEntityId>} — a non-scalar typed wrapper — and fails with "SIMPLE_PROPERTY requires
 * scalar" before checking whether the custom base class ({@link
 * com.stano.domain_jpa.springdata.EntityRepositoryImpl}) already implements the method.
 *
 * <p>The fix is to make {@code removeAllById} a {@code default} method in {@link
 * com.stano.domain_jpa.repository.EntityRepository}. Default interface methods are skipped by query
 * derivation because they already have an implementation.
 *
 * <p>This test will FAIL at context startup (not inside the test body) until the fix is applied.
 */
@DataJpaTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class EntityRepositoryRemoveAllByIdTest {

  @Autowired private DomainTestEntityRepository repository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void removeAllByIdDeletesOnlyTheRequestedEntitiesAndLeavesTheRestIntact() {
    var first = new TestEntity("first");
    var second = new TestEntity("second");
    var third = new TestEntity("third");
    repository.add(first);
    repository.add(second);
    repository.add(third);
    entityManager.flush();
    entityManager.clear();

    repository.removeAllById(List.of(first.getId(), second.getId()));
    entityManager.flush();
    entityManager.clear();

    var remaining = repository.findAll();
    assertThat(remaining).hasSize(1);
    assertThat(remaining.get(0).getId()).isEqualTo(third.getId());
  }
}
