package com.stano.domain_jpa.id;

import jakarta.persistence.AttributeConverter;
import java.util.UUID;

public abstract class EntityIdAttributeConverter<T extends EntityId>
    implements AttributeConverter<T, UUID> {
  @Override
  public UUID convertToDatabaseColumn(T attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public abstract T convertToEntityAttribute(UUID dbData);
}
