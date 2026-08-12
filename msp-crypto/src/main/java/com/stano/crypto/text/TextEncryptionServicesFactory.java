package com.stano.crypto.text;

import com.stano.crypto.text.aes256.AES256TextEncryptionServices;

/**
 * Factory for obtaining the platform's configured {@link TextEncryptionServices} implementation.
 *
 * <p>This is a static factory, not a constructible class; use {@link #getInstance()} to obtain the
 * shared {@link AES256TextEncryptionServices} instance.
 */
public final class TextEncryptionServicesFactory {
  private static final AES256TextEncryptionServices aes256TextEncryptionServices =
      new AES256TextEncryptionServices();

  /**
   * Returns the platform's default {@link TextEncryptionServices} implementation.
   *
   * @return an {@link AES256TextEncryptionServices} instance
   */
  public static TextEncryptionServices getInstance() {
    return aes256TextEncryptionServices;
  }

  private TextEncryptionServicesFactory() {}
}
