package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.binary.EncryptedBytesFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter that persists an {@link EncryptedBytes} value as its already-encrypted raw bytes.
 * Registered with {@code autoApply = true}, so it applies automatically to any entity attribute
 * typed as {@link EncryptedBytes} without further annotation.
 */
@Converter(autoApply = true)
public class EncryptedBytesAttributeConverter
    implements AttributeConverter<EncryptedBytes, byte[]> {
  /**
   * Returns the already-encrypted bytes stored by {@code attribute}, for storage in a binary
   * column.
   *
   * @param attribute the value to convert, may be {@code null}
   * @return the encrypted bytes, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public byte[] convertToDatabaseColumn(EncryptedBytes attribute) {
    return attribute == null ? null : attribute.getEncryptedBytes();
  }

  /**
   * Wraps encrypted bytes read from the database column into an {@link EncryptedBytes} value.
   *
   * @param dbData the raw encrypted bytes read from the column, may be {@code null}
   * @return an {@link EncryptedBytes} wrapping {@code dbData}, or {@code null} if {@code dbData} is
   *     {@code null}
   */
  @Override
  public EncryptedBytes convertToEntityAttribute(byte[] dbData) {
    return dbData == null ? null : EncryptedBytesFactory.getInstance().withEncryptedBytes(dbData);
  }
}
