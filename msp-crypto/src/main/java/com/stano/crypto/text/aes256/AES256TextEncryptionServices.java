package com.stano.crypto.text.aes256;

import com.stano.crypto.text.TextEncryptionServices;
import com.stano.crypto.utils.EncryptionSecretProvider;

/**
 * AES-256 implementation of {@link TextEncryptionServices}.
 *
 * <p>Delegates to a shared {@link AES256TextEncryptor} keyed with the secret resolved by {@link
 * EncryptionSecretProvider#getSecret()}. Obtain an instance via {@code
 * TextEncryptionServicesFactory.getInstance()} rather than constructing directly.
 */
public final class AES256TextEncryptionServices implements TextEncryptionServices {
  private static final AES256TextEncryptor textEncryptor =
      new AES256TextEncryptor(EncryptionSecretProvider.getSecret());

  /**
   * Encrypts the given clear text using AES-256.
   *
   * @param clearText the text to encrypt
   * @return the Base64-encoded encrypted text
   * @throws IllegalArgumentException if encryption fails
   */
  @Override
  public String encryptString(String clearText) {
    return textEncryptor.encrypt(clearText);
  }

  /**
   * Decrypts the given, previously AES-256-encrypted text.
   *
   * @param encryptedText the Base64-encoded ciphertext to decrypt
   * @return the decrypted (clear) text
   * @throws IllegalArgumentException if decryption fails
   */
  @Override
  public String decryptString(String encryptedText) {
    return textEncryptor.decrypt(encryptedText);
  }
}
