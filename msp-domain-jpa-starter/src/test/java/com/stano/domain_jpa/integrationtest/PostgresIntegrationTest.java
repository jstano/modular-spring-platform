package com.stano.domain_jpa.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = PostgresIntegrationTestConfig.class)
@TestPropertySource(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      "msp.schema.auto-install=true"
    })
@Transactional
class PostgresIntegrationTest {
  @Autowired private TestEntityRepository repository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void entityCanBePersistedAndReadBack() {
    var entity = new TestEntity("test entity", LocalDate.of(1996, 10, 6));
    repository.add(entity);
    entityManager.flush();
    entityManager.clear();

    var found = repository.get(entity.getId());
    assertThat(found.getName()).isEqualTo("test entity");
    assertThat(found.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 6));
    assertThat(found.getCreatedAt()).isNotNull();
    assertThat(found.getUpdatedAt()).isNotNull();
    assertThat(found.getVersion()).isZero();
  }

  @Test
  void auditFieldsAndVersionAreUpdatedOnModification() {
    var entity = new TestEntity("original", LocalDate.of(1990, 1, 1));
    repository.add(entity);
    entityManager.flush();
    entityManager.clear();

    var loaded = repository.get(entity.getId());
    var createdAt = loaded.getCreatedAt();
    var updatedAt = loaded.getUpdatedAt();

    loaded.setName("modified");
    entityManager.flush();
    entityManager.clear();

    var modified = repository.get(entity.getId());
    assertThat(modified.getVersion()).isEqualTo(1);
    assertThat(modified.getCreatedAt()).isEqualTo(createdAt);
    assertThat(modified.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
  }
}
