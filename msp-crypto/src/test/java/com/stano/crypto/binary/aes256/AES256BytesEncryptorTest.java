package com.stano.crypto.binary.aes256;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AES256BytesEncryptorTest {

  private final AES256BytesEncryptor encryptor = new AES256BytesEncryptor("abc123");

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
    var plainBytes = "test message".getBytes();

    var encrypted = encryptor.encrypt(plainBytes);

    assertThat(encrypted).isNotNull();
    assertThat(encrypted).isNotEqualTo(plainBytes);
    assertThat(encryptor.decrypt(encrypted)).isEqualTo(plainBytes);
  }

  @Test
  void emptyBytesShouldEncryptAndDecrypt() {
    var encrypted = encryptor.encrypt("".getBytes());
    var decrypted = encryptor.decrypt(encrypted);

    assertThat(decrypted).isEqualTo("".getBytes());
  }

  @Test
  void exceptionsThrownDuringDecryptionShouldBeWrappedAsIllegalArgumentException() {
    assertThatThrownBy(() -> encryptor.decrypt("not-valid-bytes".getBytes()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
