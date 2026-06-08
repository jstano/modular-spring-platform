package com.stano.domain_jpa.springdata.jpa;

import com.stano.crypto.text.EncryptedTextFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptedTextAttributeConverterTest {

    @Test
    void shouldConvertEncryptedTextToDatabaseColumnAndBack() {
        EncryptedTextAttributeConverter converter = new EncryptedTextAttributeConverter();
        String plainText = "sensitive-data";
        var encryptedText = EncryptedTextFactory.getInstance().withClearText(plainText);

        String dbData = converter.convertToDatabaseColumn(encryptedText);
        var result = converter.convertToEntityAttribute(dbData);

        assertThat(dbData).isEqualTo(encryptedText.getEncryptedText());
        assertThat(result.getClearText()).isEqualTo(plainText);
    }

    @Test
    void shouldHandleNullValues() {
        EncryptedTextAttributeConverter converter = new EncryptedTextAttributeConverter();

        String encrypted = converter.convertToDatabaseColumn(null);
        var decrypted = converter.convertToEntityAttribute(null);

        assertThat(encrypted).isNull();
        assertThat(decrypted).isNull();
    }

    @Test
    void shouldPreserveEncryptedFormAcrossConversions() {
        EncryptedTextAttributeConverter converter = new EncryptedTextAttributeConverter();
        String plainText = "my-secret";
        var encryptedText = EncryptedTextFactory.getInstance().withClearText(plainText);

        String dbData = converter.convertToDatabaseColumn(encryptedText);
        var reconstructed = converter.convertToEntityAttribute(dbData);
        String dbData2 = converter.convertToDatabaseColumn(reconstructed);

        assertThat(dbData).isEqualTo(dbData2);
    }
}
