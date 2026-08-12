package com.stano.crypto.binary.aes256;

import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * Encrypts and decrypts byte arrays using AES-256/GCM via Spring Security's {@link
 * AesBytesEncryptor}.
 *
 * <p>Each instance is keyed by a password supplied at construction time; a random salt is generated
 * per encryption operation via {@link KeyGenerators#secureRandom(int)}.
 */
public final class AES256BytesEncryptor {
  private final AesBytesEncryptor encryptor;

  /**
   * Creates an encryptor keyed with the given password.
   *
   * @param password the secret used to derive the encryption key
   */
  public AES256BytesEncryptor(String password) {
    this.encryptor = createEncryptor(password);
  }

  /**
   * Encrypts the given message bytes.
   *
   * @param message the bytes to encrypt, or {@code null}
   * @return the encrypted bytes, or {@code null} if {@code message} is {@code null}
   * @throws IllegalArgumentException if encryption fails
   */
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

  /**
   * Decrypts the given encrypted message bytes.
   *
   * @param encryptedMessage the bytes to decrypt, or {@code null}
   * @return the decrypted bytes, or {@code null} if {@code encryptedMessage} is {@code null}
   * @throws IllegalArgumentException if decryption fails
   */
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
