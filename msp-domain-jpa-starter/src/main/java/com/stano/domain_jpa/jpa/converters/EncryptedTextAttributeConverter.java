package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.text.EncryptedText;
import com.stano.crypto.text.EncryptedTextFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EncryptedTextAttributeConverter implements AttributeConverter<EncryptedText, String> {
  @Override
  public String convertToDatabaseColumn(EncryptedText attribute) {
    return attribute == null
           ? null
           : attribute.getEncryptedText();
  }

  @Override
  public EncryptedText convertToEntityAttribute(String dbData) {
    return dbData == null
           ? null
           : EncryptedTextFactory.getInstance().withEncryptedText(dbData);
  }
}
