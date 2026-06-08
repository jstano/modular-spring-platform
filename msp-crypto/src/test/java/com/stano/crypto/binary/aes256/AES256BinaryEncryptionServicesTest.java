package com.stano.crypto.binary.aes256;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AES256BinaryEncryptionServicesTest {

    @Test
    void shouldBeAbleToEncryptAndDecryptByteArrays() {
        var encryptionServices = new AES256BinaryEncryptionServices();

        assertThat(encryptionServices.decryptBytes(encryptionServices.encryptBytes("ABC123".getBytes())))
            .isEqualTo("ABC123".getBytes());
        assertThat(encryptionServices.decryptBytes(encryptionServices.encryptBytes("".getBytes())))
            .isEqualTo("".getBytes());
        assertThat(encryptionServices.decryptBytes(null)).isNull();
        assertThat(encryptionServices.encryptBytes(null)).isNull();
    }

    @Test
    void shouldNotBeAbleToDecryptInvalidByteArray() {
        var encryptionServices = new AES256BinaryEncryptionServices();

        assertThatThrownBy(() -> encryptionServices.decryptBytes("not-valid-bytes".getBytes()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
