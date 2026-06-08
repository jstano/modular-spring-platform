package com.stano.crypto.text.aes256;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AES256TextEncryptionServicesTest {

    @Test
    void shouldBeAbleToEncryptAndDecryptStrings() {
        var encryptionServices = new AES256TextEncryptionServices();

        assertThat(encryptionServices.decryptString(encryptionServices.encryptString("ABC123"))).isEqualTo("ABC123");
        assertThat(encryptionServices.decryptString(encryptionServices.encryptString(""))).isEqualTo("");
        assertThat(encryptionServices.decryptString(null)).isNull();
        assertThat(encryptionServices.encryptString(null)).isNull();
    }

    @Test
    void shouldNotBeAbleToDecryptInvalidBase64String() {
        var encryptionServices = new AES256TextEncryptionServices();

        assertThatThrownBy(() -> encryptionServices.decryptString("!!!not-valid-base64!!!"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
