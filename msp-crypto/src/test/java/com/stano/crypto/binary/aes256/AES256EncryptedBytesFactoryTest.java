package com.stano.crypto.binary.aes256;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AES256EncryptedBytesFactoryTest {

  @Test
  void withPlainBytesShouldReturnInstanceOfAES256EncryptedBytesAndShouldWork() {
    var factory = new AES256EncryptedBytesFactory();

    assertThat(factory.withClearBytes("abc123".getBytes()))
        .isInstanceOf(AES256EncryptedBytes.class);
    assertThat(factory.withClearBytes("abc123".getBytes()).getClearBytes())
        .isEqualTo("abc123".getBytes());
  }

  @Test
  void withEncryptedBytesShouldReturnInstanceOfAES256EncryptedBytesAndShouldWork() {
    var factory = new AES256EncryptedBytesFactory();
    var encryptedBytes = new AES256BinaryEncryptionServices().encryptBytes("abc123".getBytes());

    assertThat(factory.withEncryptedBytes(encryptedBytes)).isInstanceOf(AES256EncryptedBytes.class);
    assertThat(factory.withEncryptedBytes(encryptedBytes).getClearBytes())
        .isEqualTo("abc123".getBytes());
  }
}
