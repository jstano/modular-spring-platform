package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.password.Password;
import com.stano.crypto.password.PasswordFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter that persists a {@link Password} value as its already-hashed/encrypted string
 * representation. Registered with {@code autoApply = true}, so it applies automatically to any
 * entity attribute typed as {@link Password} without further annotation.
 */
@Converter(autoApply = true)
public class PasswordAttributeConverter implements AttributeConverter<Password, String> {
  /**
   * Returns the already-encrypted representation stored by {@code attribute}, for storage in a text
   * column.
   *
   * @param attribute the value to convert, may be {@code null}
   * @return the encrypted password text, or {@code null} if {@code attribute} is {@code null}
   */
  @Override
  public String convertToDatabaseColumn(Password attribute) {
    return attribute == null ? null : attribute.getEncryptedPassword();
  }

  /**
   * Wraps an encrypted password value read from the database column into a {@link Password}.
   *
   * @param dbData the raw encrypted text read from the column, may be {@code null}
   * @return a {@link Password} wrapping {@code dbData}, or {@code null} if {@code dbData} is {@code
   *     null}
   */
  @Override
  public Password convertToEntityAttribute(String dbData) {
    return dbData == null ? null : PasswordFactory.getInstance().withEncryptedText(dbData);
  }
}
