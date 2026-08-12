package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.text.EncryptedText;
import com.stano.crypto.text.EncryptedTextFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter that persists an {@link EncryptedText} value as its already-encrypted string
 * representation. Registered with {@code autoApply = true}, so it applies automatically to any
 * entity attribute typed as {@link EncryptedText} without further annotation.
 */
@Converter(autoApply = true)
public class EncryptedTextAttributeConverter implements AttributeConverter<EncryptedText, String> {
  /**
   * Returns the already-encrypted text stored by {@code attribute}, for storage in a text column.
   *
   * @param attribute the value to convert, may be {@code null}
   * @return the encrypted text, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public String convertToDatabaseColumn(EncryptedText attribute) {
    return attribute == null ? null : attribute.getEncryptedText();
  }

  /**
   * Wraps encrypted text read from the database column into an {@link EncryptedText} value.
   *
   * @param dbData the raw encrypted text read from the column, may be {@code null}
   * @return an {@link EncryptedText} wrapping {@code dbData}, or {@code null} if {@code dbData} is
   *     {@code null}
   */
  @Override
  public EncryptedText convertToEntityAttribute(String dbData) {
    return dbData == null ? null : EncryptedTextFactory.getInstance().withEncryptedText(dbData);
  }
}
