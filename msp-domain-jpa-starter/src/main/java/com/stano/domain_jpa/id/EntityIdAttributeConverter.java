package com.stano.domain_jpa.id;

import jakarta.persistence.AttributeConverter;
import java.util.UUID;

/**
 * Base JPA converter for {@link EntityId} subtypes, handling the common conversion to a UUID
 * database column. Concrete subclasses implement {@link #convertToEntityAttribute(UUID)} to wrap
 * the raw UUID back into their specific {@link EntityId} type, typically annotated with
 * {@code @Converter(autoApply = true)}.
 *
 * @param <T> the concrete {@link EntityId} subtype handled by this converter
 */
public abstract class EntityIdAttributeConverter<T extends EntityId>
    implements AttributeConverter<T, UUID> {
  /**
   * Converts a typed id to the UUID stored in the database column.
   *
   * @param attribute the identifier to convert, may be {@code null}
   * @return the underlying UUID, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public UUID convertToDatabaseColumn(T attribute) {
    return attribute == null ? null : attribute.value();
  }

  /**
   * Converts a database UUID column value into this converter's specific {@link EntityId} subtype.
   *
   * @param dbData the raw UUID column value, may be {@code null}
   * @return the typed identifier wrapping {@code dbData}, or {@code null} if {@code dbData} is
   *     {@code null}
   */
  @Override
  public abstract T convertToEntityAttribute(UUID dbData);
}
