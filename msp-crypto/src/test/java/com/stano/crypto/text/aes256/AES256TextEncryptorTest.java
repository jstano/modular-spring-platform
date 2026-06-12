package com.stano.crypto.text.aes256;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AES256TextEncryptorTest {

  private final AES256TextEncryptor encryptor = new AES256TextEncryptor("abc123");

  @Test
  void encryptShouldReturnNullWhenNullValueIsPassedIn() {
    assertThat(encryptor.encrypt(null)).isNull();
  }

  @Test
  void decryptShouldReturnNullWhenNullValueIsPassedIn() {
    assertThat(encryptor.decrypt(null)).isNull();
  }

  @Test
  void encryptAndDecryptShouldBeReversible() {
    var plaintext = "test message";
    var encrypted = encryptor.encrypt(plaintext);

    assertThat(encrypted).isNotNull();
    assertThat(encrypted).isNotEqualTo(plaintext);
    assertThat(encryptor.decrypt(encrypted)).isEqualTo(plaintext);
  }

  @Test
  void emptyStringsShouldEncryptAndDecrypt() {
    var encrypted = encryptor.encrypt("");
    var decrypted = encryptor.decrypt(encrypted);

    assertThat(decrypted).isEqualTo("");
  }

  @Test
  void exceptionsThrownDuringDecryptionShouldBeWrappedAsIllegalArgumentException() {
    assertThatThrownBy(() -> encryptor.decrypt("not-valid-base64!"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
