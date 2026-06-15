package com.stano.domain_jpa.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TestConfig.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@Transactional
class JpaIntegrationTest {
  @Autowired private TestEntityRepository repository;

  @Test
  void entityCanBePersistedWhenEntityPackagesAttributeIsUsed() {
    var entity = new TestEntity("test", LocalDate.of(1996, 10, 6));
    repository.add(entity);

    var found = repository.findById(entity.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("test");
  }
}
