package com.stano.domain_jpa.id;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class EntityIdAttributeConverterTest {
  static class TestId extends EntityId {
    TestId(UUID value) {
      super(value);
    }
  }

  static class TestIdConverter extends EntityIdAttributeConverter {
    @Override
    public EntityId convertToEntityAttribute(UUID dbData) {
      return dbData == null ? null : new TestId(dbData);
    }
  }

  @Test
  void shouldConvertEntityIdToDatabaseColumn() {
    TestIdConverter converter = new TestIdConverter();
    UUID uuid = UUID.randomUUID();
    EntityId entityId = new TestId(uuid);

    UUID result = converter.convertToDatabaseColumn(entityId);

    assertThat(result).isEqualTo(uuid);
  }

  @Test
  void shouldConvertUuidToEntityAttribute() {
    TestIdConverter converter = new TestIdConverter();
    UUID uuid = UUID.randomUUID();

    EntityId result = converter.convertToEntityAttribute(uuid);

    assertThat(result.value()).isEqualTo(uuid);
  }

  @Test
  void shouldHandleNullInConvertToDatabaseColumn() {
    TestIdConverter converter = new TestIdConverter();

    UUID result = converter.convertToDatabaseColumn(null);

    assertThat(result).isNull();
  }

  @Test
  void shouldHandleNullInConvertToEntityAttribute() {
    TestIdConverter converter = new TestIdConverter();

    EntityId result = converter.convertToEntityAttribute(null);

    assertThat(result).isNull();
  }

  @Test
  void shouldPreserveUuidAcrossRoundTrip() {
    TestIdConverter converter = new TestIdConverter();
    UUID originalUuid = UUID.randomUUID();

    EntityId entityId = converter.convertToEntityAttribute(originalUuid);
    UUID roundTripUuid = converter.convertToDatabaseColumn(entityId);

    assertThat(roundTripUuid).isEqualTo(originalUuid);
  }

  @Test
  void twoInstancesWithSameUuidShouldBeEqual() {
    UUID uuid = UUID.randomUUID();
    EntityId id1 = new TestId(uuid);
    EntityId id2 = new TestId(uuid);

    assertThat(id1).isEqualTo(id2);
    assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
  }
}
