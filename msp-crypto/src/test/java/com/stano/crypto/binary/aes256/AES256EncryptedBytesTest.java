package com.stano.crypto.binary.aes256;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AES256EncryptedBytesTest {

  @Test
  void testBasicStuff() {
    var encryptedBytes = new AES256BinaryEncryptionServices().encryptBytes("abc123".getBytes());
    var aes256EncryptedBytes = new AES256EncryptedBytes(encryptedBytes);

    assertThat(aes256EncryptedBytes.getEncryptedBytes()).isEqualTo(encryptedBytes);
    assertThat(aes256EncryptedBytes.getClearBytes()).isEqualTo("abc123".getBytes());
  }

  @Test
  void testEquals() {
    var encryptedBytes1 = new AES256BinaryEncryptionServices().encryptBytes("abc123".getBytes());
    var encryptedBytes2 = new AES256BinaryEncryptionServices().encryptBytes("123abc".getBytes());

    var aes256EncryptedBytes1a = new AES256EncryptedBytes(encryptedBytes1);
    var aes256EncryptedBytes1b = new AES256EncryptedBytes(encryptedBytes1);
    var aes256EncryptedBytes2 = new AES256EncryptedBytes(encryptedBytes2);

    assertThat(aes256EncryptedBytes1a).isEqualTo(aes256EncryptedBytes1a);
    assertThat(aes256EncryptedBytes1a).isEqualTo(aes256EncryptedBytes1b);
    assertThat(aes256EncryptedBytes1a).isNotEqualTo(aes256EncryptedBytes2);
    assertThat(aes256EncryptedBytes1a).isNotEqualTo("abc123");
  }

  @Test
  void testHashCode() {
    var encryptedBytes1 = new AES256BinaryEncryptionServices().encryptBytes("abc123".getBytes());
    var encryptedBytes2 = new AES256BinaryEncryptionServices().encryptBytes("123abc".getBytes());

    var aes256EncryptedBytes1a = new AES256EncryptedBytes(encryptedBytes1);
    var aes256EncryptedBytes1b = new AES256EncryptedBytes(encryptedBytes1);
    var aes256EncryptedBytes2 = new AES256EncryptedBytes(encryptedBytes2);

    assertThat(aes256EncryptedBytes1a.hashCode()).isEqualTo(aes256EncryptedBytes1a.hashCode());
    assertThat(aes256EncryptedBytes1a.hashCode()).isEqualTo(aes256EncryptedBytes1b.hashCode());
    assertThat(aes256EncryptedBytes1a.hashCode()).isNotEqualTo(aes256EncryptedBytes2.hashCode());
  }
}
