package com.stano.crypto.text.aes256;

import java.util.Base64;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * Encrypts and decrypts text using AES-256/GCM via Spring Security's {@link AesBytesEncryptor},
 * exchanging ciphertext as Base64-encoded strings.
 *
 * <p>Each instance is keyed by a password supplied at construction time; a random salt is generated
 * per encryption operation via {@link KeyGenerators#secureRandom(int)}. Plain text is converted
 * to/from bytes using UTF-8.
 */
public final class AES256TextEncryptor {
  private final AesBytesEncryptor encryptor;

  /**
   * Creates an encryptor keyed with the given password.
   *
   * @param password the secret used to derive the encryption key
   */
  public AES256TextEncryptor(String password) {
    this.encryptor = createEncryptor(password);
  }

  /**
   * Encrypts the given message and returns the ciphertext as a Base64-encoded string.
   *
   * @param message the text to encrypt, or {@code null}
   * @return the Base64-encoded encrypted text, or {@code null} if {@code message} is {@code null}
   * @throws IllegalArgumentException if encryption fails
   */
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

  /**
   * Decrypts a Base64-encoded, previously encrypted message.
   *
   * @param encryptedMessage the Base64-encoded ciphertext to decrypt, or {@code null}
   * @return the decrypted (clear) text, or {@code null} if {@code encryptedMessage} is {@code null}
   * @throws IllegalArgumentException if decryption fails
   */
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
