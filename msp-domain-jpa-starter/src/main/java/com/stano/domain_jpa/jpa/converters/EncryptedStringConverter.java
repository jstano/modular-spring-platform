package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.text.TextEncryptionServicesFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
  @Override
  public String convertToDatabaseColumn(String attribute) {
    return attribute == null
        ? null
        : TextEncryptionServicesFactory.getInstance().encryptString(attribute);
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    return dbData == null
        ? null
        : TextEncryptionServicesFactory.getInstance().decryptString(dbData);
  }
}
