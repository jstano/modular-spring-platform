package com.stano.domain_jpa.springdata.jpa;

import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.binary.EncryptedBytesFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EncryptedBytesAttributeConverter implements AttributeConverter<EncryptedBytes, byte[]> {
  @Override
  public byte[] convertToDatabaseColumn(EncryptedBytes attribute) {
    return attribute == null
           ? null
           : attribute.getEncryptedBytes();
  }

  @Override
  public EncryptedBytes convertToEntityAttribute(byte[] dbData) {
    return dbData == null
           ? null
           : EncryptedBytesFactory.getInstance().withEncryptedBytes(dbData);
  }
}
