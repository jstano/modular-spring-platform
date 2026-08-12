package com.stano.domain_jpa.id;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

/**
 * JPA converter that maps a {@link DatabaseId} to and from its underlying {@link UUID} column
 * value. Registered with {@code autoApply = true}, so it applies automatically to any entity
 * attribute typed as {@link DatabaseId} without further annotation.
 */
@Converter(autoApply = true)
public class DatabaseIdAttributeConverter implements AttributeConverter<DatabaseId, UUID> {
  /**
   * Converts a {@link DatabaseId} to the UUID stored in the database column.
   *
   * @param attribute the identifier to convert, may be {@code null}
   * @return the underlying UUID, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public UUID convertToDatabaseColumn(DatabaseId attribute) {
    return attribute == null ? null : attribute.value();
  }

  /**
   * Converts a database UUID column value into a {@link DatabaseId}.
   *
   * @param dbData the raw UUID column value, may be {@code null}
   * @return a {@link DatabaseId} wrapping {@code dbData}, or {@code null} if {@code dbData} is
   *     {@code null}
   */
  @Override
  public DatabaseId convertToEntityAttribute(UUID dbData) {
    return dbData == null ? null : () -> dbData;
  }
}
