package com.stano.domain_jpa.jpa.converters;

import com.stano.crypto.password.Password;
import com.stano.crypto.password.PasswordFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PasswordAttributeConverter implements AttributeConverter<Password, String> {
  @Override
  public String convertToDatabaseColumn(Password attribute) {
    return attribute == null
           ? null
           : attribute.getEncryptedPassword();
  }

  @Override
  public Password convertToEntityAttribute(String dbData) {
    return dbData == null
           ? null
           : PasswordFactory.getInstance().withEncryptedText(dbData);
  }
}
