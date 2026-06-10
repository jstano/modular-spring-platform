package com.stano.domain_jpa.jpa.converters;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptedStringConverterTest {

    @Test
    void shouldEncryptAndDecryptStringBackToOriginal() {
        EncryptedStringConverter converter = new EncryptedStringConverter();
        String originalText = "sensitive-data@example.com";

        String encrypted = converter.convertToDatabaseColumn(originalText);
        String decrypted = converter.convertToEntityAttribute(encrypted);

        assertThat(encrypted)
            .isNotNull()
            .isNotEqualTo(originalText);
        assertThat(decrypted).isEqualTo(originalText);
    }

    @Test
    void shouldHandleNullValues() {
        EncryptedStringConverter converter = new EncryptedStringConverter();

        String encrypted = converter.convertToDatabaseColumn(null);
        String decrypted = converter.convertToEntityAttribute(null);

        assertThat(encrypted).isNull();
        assertThat(decrypted).isNull();
    }

    @Test
    void shouldProduceDifferentCiphertextsForMultipleEncryptionsDueToRandomIV() {
        EncryptedStringConverter converter = new EncryptedStringConverter();
        String originalText = "sensitive-data@example.com";

        String encrypted1 = converter.convertToDatabaseColumn(originalText);
        String encrypted2 = converter.convertToDatabaseColumn(originalText);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
        assertThat(converter.convertToEntityAttribute(encrypted1)).isEqualTo(originalText);
        assertThat(converter.convertToEntityAttribute(encrypted2)).isEqualTo(originalText);
    }
}
