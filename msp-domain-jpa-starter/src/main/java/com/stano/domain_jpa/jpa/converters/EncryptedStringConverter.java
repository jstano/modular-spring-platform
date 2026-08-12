package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.text.TextEncryptionServicesFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter that transparently encrypts a plain {@link String} attribute before storing it and
 * decrypts it back on load. Unlike the platform's {@link com.stano.crypto.text.EncryptedText} /
 * {@link com.stano.crypto.binary.EncryptedBytes} value types, the entity attribute itself remains a
 * plain {@code String}; only the database column is encrypted. Not auto-applied — annotate
 * individual fields with {@code @Convert(converter = EncryptedStringConverter.class)}.
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
  /**
   * Encrypts a plain string value for storage.
   *
   * @param attribute the plain-text value to encrypt, may be {@code null}
   * @return the encrypted value, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public String convertToDatabaseColumn(String attribute) {
    return attribute == null
        ? null
        : TextEncryptionServicesFactory.getInstance().encryptString(attribute);
  }

  /**
   * Decrypts a value read from the database column back into plain text.
   *
   * @param dbData the encrypted column value, may be {@code null}
   * @return the decrypted plain-text value, or {@code null} if {@code dbData} is {@code null}
   */
  @Override
  public String convertToEntityAttribute(String dbData) {
    return dbData == null
        ? null
        : TextEncryptionServicesFactory.getInstance().decryptString(dbData);
  }
}
