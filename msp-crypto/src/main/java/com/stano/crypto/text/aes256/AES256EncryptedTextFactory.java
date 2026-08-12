package com.stano.crypto.text.aes256;

import com.stano.crypto.text.EncryptedText;
import com.stano.crypto.text.EncryptedTextFactory;
import com.stano.crypto.text.TextEncryptionServicesFactory;

/**
 * AES-256 implementation of {@link EncryptedTextFactory}.
 *
 * <p>Obtain an instance via {@link EncryptedTextFactory#getInstance()}.
 */
public final class AES256EncryptedTextFactory implements EncryptedTextFactory {
  /**
   * Encrypts the given clear text using AES-256 and wraps the result.
   *
   * @param clearText the unencrypted text to encrypt
   * @return an {@link AES256EncryptedText} holding the encrypted form of {@code clearText}
   * @throws IllegalArgumentException if encryption fails
   */
  @Override
  public EncryptedText withClearText(String clearText) {
    return new AES256EncryptedText(
        TextEncryptionServicesFactory.getInstance().encryptString(clearText));
  }

  /**
   * Wraps already AES-256-encrypted text without re-encrypting it.
   *
   * @param encryptedText the previously encrypted text
   * @return an {@link AES256EncryptedText} holding {@code encryptedText}
   */
  @Override
  public EncryptedText withEncryptedText(String encryptedText) {
    return new AES256EncryptedText(encryptedText);
  }
}
