package com.stano.crypto.binary;

import com.stano.crypto.binary.aes256.AES256BinaryEncryptionServices;

/**
 * Factory for obtaining the platform's configured {@link BinaryEncryptionServices} implementation.
 *
 * <p>This is a static factory, not a constructible class; use {@link #getInstance()} to obtain the
 * shared {@link AES256BinaryEncryptionServices} instance.
 */
public final class BinaryEncryptionServicesFactory {
  private static final AES256BinaryEncryptionServices aes256TextEncryptionServices =
      new AES256BinaryEncryptionServices();

  /**
   * Returns the platform's default {@link BinaryEncryptionServices} implementation.
   *
   * @return an {@link AES256BinaryEncryptionServices} instance
   */
  public static BinaryEncryptionServices getInstance() {
    return aes256TextEncryptionServices;
  }

  private BinaryEncryptionServicesFactory() {}
}
