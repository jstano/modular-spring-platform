package com.stano.domain_jpa.springdata.jpa;

import com.stano.crypto.password.PasswordFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordAttributeConverterTest {

    @Test
    void shouldConvertPasswordToDatabaseColumnAndBack() {
        PasswordAttributeConverter converter = new PasswordAttributeConverter();
        String plainPassword = "my-secure-password";
        var password = PasswordFactory.getInstance().withClearText(plainPassword);

        String dbData = converter.convertToDatabaseColumn(password);
        var result = converter.convertToEntityAttribute(dbData);

        assertThat(dbData).isEqualTo(password.getEncryptedPassword());
        assertThat(result.matches(plainPassword)).isTrue();
    }

    @Test
    void shouldHandleNullValues() {
        PasswordAttributeConverter converter = new PasswordAttributeConverter();

        String encrypted = converter.convertToDatabaseColumn(null);
        var decrypted = converter.convertToEntityAttribute(null);

        assertThat(encrypted).isNull();
        assertThat(decrypted).isNull();
    }

    @Test
    void shouldPreserveEncryptedFormAcrossConversions() {
        PasswordAttributeConverter converter = new PasswordAttributeConverter();
        String plainPassword = "my-secure-password";
        var password = PasswordFactory.getInstance().withClearText(plainPassword);

        String dbData = converter.convertToDatabaseColumn(password);
        var reconstructed = converter.convertToEntityAttribute(dbData);
        String dbData2 = converter.convertToDatabaseColumn(reconstructed);

        assertThat(dbData).isEqualTo(dbData2);
    }
}
