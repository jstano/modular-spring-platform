package com.stano.domain_jpa.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "msp.schema.auto-install=true")
@Transactional
class SchemaInstallAndMigrateIntegrationTest {
  @Autowired private TestEntityRepository repository;

  @Test
  void entityCanBePersistedAndRetrieved() {
    var entity = new TestEntity("test entity", LocalDate.of(1996, 10, 6));
    repository.add(entity);

    var found = repository.findById(entity.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("test entity");
    assertThat(found.get().getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 6));
  }

  @Test
  void multipleEntitiesCanBePersisted() {
    var entity1 = new TestEntity("first", LocalDate.of(1990, 1, 1));
    var entity2 = new TestEntity("second", LocalDate.of(2000, 2, 2));
    repository.add(entity1);
    repository.add(entity2);

    var all = repository.findAll();
    assertThat(all).hasSize(2);
  }
}
