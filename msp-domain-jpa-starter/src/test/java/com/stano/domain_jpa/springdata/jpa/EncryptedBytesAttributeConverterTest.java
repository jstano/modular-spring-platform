package com.stano.domain_jpa.springdata.jpa;

import com.stano.crypto.binary.EncryptedBytesFactory;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptedBytesAttributeConverterTest {

    @Test
    void shouldConvertEncryptedBytesToDatabaseColumnAndBack() {
        EncryptedBytesAttributeConverter converter = new EncryptedBytesAttributeConverter();
        byte[] plainBytes = "sensitive-binary-data".getBytes(StandardCharsets.UTF_8);
        var encryptedBytes = EncryptedBytesFactory.getInstance().withClearBytes(plainBytes);

        byte[] dbData = converter.convertToDatabaseColumn(encryptedBytes);
        var result = converter.convertToEntityAttribute(dbData);

        assertThat(dbData).isEqualTo(encryptedBytes.getEncryptedBytes());
        assertThat(result.getClearBytes()).isEqualTo(plainBytes);
    }

    @Test
    void shouldHandleNullValues() {
        EncryptedBytesAttributeConverter converter = new EncryptedBytesAttributeConverter();

        byte[] encrypted = converter.convertToDatabaseColumn(null);
        var decrypted = converter.convertToEntityAttribute(null);

        assertThat(encrypted).isNull();
        assertThat(decrypted).isNull();
    }

    @Test
    void shouldPreserveEncryptedFormAcrossConversions() {
        EncryptedBytesAttributeConverter converter = new EncryptedBytesAttributeConverter();
        byte[] plainBytes = "my-secret-bytes".getBytes(StandardCharsets.UTF_8);
        var encryptedBytes = EncryptedBytesFactory.getInstance().withClearBytes(plainBytes);

        byte[] dbData = converter.convertToDatabaseColumn(encryptedBytes);
        var reconstructed = converter.convertToEntityAttribute(dbData);
        byte[] dbData2 = converter.convertToDatabaseColumn(reconstructed);

        assertThat(dbData).isEqualTo(dbData2);
    }
}
