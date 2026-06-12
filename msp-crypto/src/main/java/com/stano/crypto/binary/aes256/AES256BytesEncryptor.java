package com.stano.crypto.binary.aes256;

import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public final class AES256BytesEncryptor {
  private final AesBytesEncryptor encryptor;

  public AES256BytesEncryptor(String password) {
    this.encryptor = createEncryptor(password);
  }

  public byte[] encrypt(byte[] message) {
    if (message == null) {
      return null;
    }

    try {
      return encryptor.encrypt(message);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to encrypt bytes", e);
    }
  }

  public byte[] decrypt(byte[] encryptedMessage) {
    if (encryptedMessage == null) {
      return null;
    }

    try {
      return encryptor.decrypt(encryptedMessage);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to decrypt bytes", e);
    }
  }

  private static AesBytesEncryptor createEncryptor(String password) {
    return new AesBytesEncryptor(
        password,
        "5c0744940b5c369b",
        KeyGenerators.secureRandom(16),
        AesBytesEncryptor.CipherAlgorithm.GCM);
  }
}
