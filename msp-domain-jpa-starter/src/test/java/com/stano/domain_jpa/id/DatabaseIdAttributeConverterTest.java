package com.stano.domain_jpa.id;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DatabaseIdAttributeConverterTest {

  @Test
  void shouldConvertDatabaseIdToDatabaseColumn() {
    DatabaseIdAttributeConverter converter = new DatabaseIdAttributeConverter();
    UUID uuid = UUID.randomUUID();
    DatabaseId databaseId = () -> uuid;

    UUID result = converter.convertToDatabaseColumn(databaseId);

    assertThat(result).isEqualTo(uuid);
  }

  @Test
  void shouldConvertUuidToEntityAttribute() {
    DatabaseIdAttributeConverter converter = new DatabaseIdAttributeConverter();
    UUID uuid = UUID.randomUUID();

    DatabaseId result = converter.convertToEntityAttribute(uuid);

    assertThat(result.value()).isEqualTo(uuid);
  }

  @Test
  void shouldHandleNullInConvertToDatabaseColumn() {
    DatabaseIdAttributeConverter converter = new DatabaseIdAttributeConverter();

    UUID result = converter.convertToDatabaseColumn(null);

    assertThat(result).isNull();
  }

  @Test
  void shouldHandleNullInConvertToEntityAttribute() {
    DatabaseIdAttributeConverter converter = new DatabaseIdAttributeConverter();

    DatabaseId result = converter.convertToEntityAttribute(null);

    assertThat(result).isNull();
  }

  @Test
  void shouldPreserveUuidAcrossRoundTrip() {
    DatabaseIdAttributeConverter converter = new DatabaseIdAttributeConverter();
    UUID originalUuid = UUID.randomUUID();

    DatabaseId databaseId = converter.convertToEntityAttribute(originalUuid);
    UUID roundTripUuid = converter.convertToDatabaseColumn(databaseId);

    assertThat(roundTripUuid).isEqualTo(originalUuid);
  }
}
