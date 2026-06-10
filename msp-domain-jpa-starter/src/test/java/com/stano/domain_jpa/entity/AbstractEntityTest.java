package com.stano.domain_jpa.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractEntityTest {

  @Test
  void entityIsEqualToItself() {
    var entity = new TestEntity();

    assertThat(entity).isEqualTo(entity);
  }

  @Test
  void twoEntitiesWithTheSameIdAreEqual() throws Exception {
    var a = new TestEntity();
    var b = new TestEntity();
    setId(b, getId(a));

    assertThat(a).isEqualTo(b);
  }

  @Test
  void twoEntitiesWithDifferentIdsAreNotEqual() {
    var a = new TestEntity();
    var b = new TestEntity();

    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void entityIsNotEqualToNull() {
    var entity = new TestEntity();

    assertThat(entity).isNotEqualTo(null);
  }

  @Test
  void entitiesOfDifferentConcreteTypesWithTheSameIdAreNotEqual() throws Exception {
    var a = new TestEntity();
    var b = new AnotherTestEntity();
    setId(b, getId(a));

    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equalEntitiesHaveTheSameHashCode() throws Exception {
    var a = new TestEntity();
    var b = new TestEntity();
    setId(b, getId(a));

    assertThat(a.hashCode()).isEqualTo(b.hashCode());
  }

  @Test
  void hashCodeIsConsistentAcrossMultipleCalls() {
    var entity = new TestEntity();

    assertThat(entity.hashCode()).isEqualTo(entity.hashCode());
  }

  private static UUID getId(AbstractEntity<?> entity) throws Exception {
    Field field = AbstractEntity.class.getDeclaredField("id");
    field.setAccessible(true);
    return (UUID) field.get(entity);
  }

  private static void setId(AbstractEntity<?> entity, UUID id) throws Exception {
    Field field = AbstractEntity.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(entity, id);
  }
}
