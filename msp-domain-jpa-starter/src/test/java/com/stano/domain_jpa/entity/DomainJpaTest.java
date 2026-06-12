package com.stano.domain_jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.domain_jpa.EntityInstancio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class DomainJpaTest {
  @Autowired private TestEntityRepository repository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void entityCanBePersistedAndReadBack() {
    var entity = EntityInstancio.of(TestEntity.class).create();
    repository.add(entity);
    entityManager.flush();
    entityManager.clear();

    var found = repository.get(entity.getId());
    assertThat(found.getId()).isEqualTo(entity.getId());
    assertThat(found.getName()).isEqualTo(entity.getName());
    assertThat(found.getBirthDate()).isEqualTo(entity.getBirthDate());
    assertThat(found.getBirthDateTime()).isEqualTo(entity.getBirthDateTime());
    assertThat(found.getBirthTime()).isEqualTo(entity.getBirthTime());
    assertThat(found.getBirthDayOfWeek()).isEqualTo(entity.getBirthDayOfWeek());
    assertThat(found.getLocale()).isEqualTo(entity.getLocale());
    assertThat(found.getZoneId()).isEqualTo(entity.getZoneId());
    assertThat(found.getUrl()).isEqualTo(entity.getUrl());
    assertThat(found.getDecimal()).isEqualByComparingTo(entity.getDecimal());
    assertThat(found.isActive()).isEqualTo(entity.isActive());
    assertThat(found.getEncryptedBytes()).isEqualTo(entity.getEncryptedBytes());
    assertThat(found.getEncryptedText()).isEqualTo(entity.getEncryptedText());
    assertThat(found.getPassword()).isEqualTo(entity.getPassword());
    assertThat(found.getEncryptedString()).isEqualTo(entity.getEncryptedString());
    assertThat(found.getCurrency()).isEqualTo(entity.getCurrency());
    assertThat(found.getCreatedAt()).isNotNull();
    assertThat(found.getUpdatedAt()).isNotNull();
    assertThat(found.getVersion()).isZero();
  }

  @Test
  void allFieldsCanBeUpdatedAndSaved() {
    var entity = EntityInstancio.of(TestEntity.class).create();
    repository.add(entity);
    entityManager.flush();
    entityManager.clear();

    var loaded = repository.get(entity.getId());
    var updated = EntityInstancio.of(TestEntity.class).create();
    loaded.setName(updated.getName());
    loaded.setBirthDate(updated.getBirthDate());
    loaded.setBirthDateTime(updated.getBirthDateTime());
    loaded.setBirthTime(updated.getBirthTime());
    loaded.setBirthDayOfWeek(updated.getBirthDayOfWeek());
    loaded.setLocale(updated.getLocale());
    loaded.setZoneId(updated.getZoneId());
    loaded.setUrl(updated.getUrl());
    loaded.setDecimal(updated.getDecimal());
    loaded.setActive(updated.isActive());
    loaded.setEncryptedBytes(updated.getEncryptedBytes());
    loaded.setEncryptedText(updated.getEncryptedText());
    loaded.setPassword(updated.getPassword());
    loaded.setEncryptedString(updated.getEncryptedString());
    loaded.setCurrency(updated.getCurrency());
    entityManager.flush();
    entityManager.clear();

    var found = repository.get(entity.getId());
    assertThat(found.getName()).isEqualTo(updated.getName());
    assertThat(found.getBirthDate()).isEqualTo(updated.getBirthDate());
    assertThat(found.getBirthDateTime()).isEqualTo(updated.getBirthDateTime());
    assertThat(found.getBirthTime()).isEqualTo(updated.getBirthTime());
    assertThat(found.getBirthDayOfWeek()).isEqualTo(updated.getBirthDayOfWeek());
    assertThat(found.getLocale()).isEqualTo(updated.getLocale());
    assertThat(found.getZoneId()).isEqualTo(updated.getZoneId());
    assertThat(found.getUrl()).isEqualTo(updated.getUrl());
    assertThat(found.getDecimal()).isEqualByComparingTo(updated.getDecimal());
    assertThat(found.isActive()).isEqualTo(updated.isActive());
    assertThat(found.getEncryptedBytes()).isEqualTo(updated.getEncryptedBytes());
    assertThat(found.getEncryptedText()).isEqualTo(updated.getEncryptedText());
    assertThat(found.getPassword()).isEqualTo(updated.getPassword());
    assertThat(found.getEncryptedString()).isEqualTo(updated.getEncryptedString());
    assertThat(found.getCurrency()).isEqualTo(updated.getCurrency());
  }

  @Test
  void auditFieldsAndVersionAreUpdatedOnModification() {
    var entity = new TestEntity("original");
    repository.add(entity);
    entityManager.flush();
    entityManager.clear();

    var loaded = repository.get(entity.getId());
    assertThat(loaded.getVersion()).isZero();
    assertThat(loaded.getCreatedAt()).isNotNull();
    assertThat(loaded.getUpdatedAt()).isNotNull();
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
