package com.stano.domain_jpa.id;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class DatabaseIdAttributeConverter implements AttributeConverter<DatabaseId, UUID> {
  @Override
  public UUID convertToDatabaseColumn(DatabaseId attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public DatabaseId convertToEntityAttribute(UUID dbData) {
    return dbData == null ? null : () -> dbData;
  }
}
