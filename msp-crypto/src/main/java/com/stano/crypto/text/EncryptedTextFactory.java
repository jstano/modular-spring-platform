package com.stano.crypto.text;

import com.stano.crypto.text.aes256.AES256EncryptedTextFactory;

/**
 * Creates {@link EncryptedText} instances from clear or already-encrypted text.
 *
 * <p>Obtain an instance via {@link #getInstance()}, which currently returns an {@link
 * AES256EncryptedTextFactory}.
 */
public interface EncryptedTextFactory {
  /**
   * Returns the platform's default {@link EncryptedTextFactory} implementation.
   *
   * @return an {@link AES256EncryptedTextFactory} instance
   */
  static EncryptedTextFactory getInstance() {
    return new AES256EncryptedTextFactory();
  }

  /**
   * Encrypts the given clear text and wraps the result.
   *
   * @param clearText the unencrypted text to encrypt
   * @return an {@link EncryptedText} holding the encrypted form of {@code clearText}
   */
  EncryptedText withClearText(String clearText);

  /**
   * Wraps already-encrypted text without re-encrypting it.
   *
   * @param encryptedText the previously encrypted text
   * @return an {@link EncryptedText} holding {@code encryptedText}
   */
  EncryptedText withEncryptedText(String encryptedText);
}
