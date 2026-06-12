package com.stano.crypto.text.aes256;

import java.util.Base64;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public final class AES256TextEncryptor {
  private final AesBytesEncryptor encryptor;

  public AES256TextEncryptor(String password) {
    this.encryptor = createEncryptor(password);
  }

  public String encrypt(String message) {
    if (message == null) {
      return null;
    }

    try {
      byte[] messageBytes = message.getBytes("UTF-8");
      byte[] encryptedBytes = encryptor.encrypt(messageBytes);
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to encrypt message", e);
    }
  }

  public String decrypt(String encryptedMessage) {
    if (encryptedMessage == null) {
      return null;
    }

    try {
      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
      byte[] decryptedBytes = encryptor.decrypt(encryptedBytes);
      return new String(decryptedBytes, "UTF-8");
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to decrypt message", e);
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
