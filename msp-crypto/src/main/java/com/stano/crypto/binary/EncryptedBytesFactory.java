package com.stano.crypto.binary;

import com.stano.crypto.binary.aes256.AES256EncryptedBytesFactory;

/**
 * Creates {@link EncryptedBytes} instances from clear or already-encrypted byte arrays.
 *
 * <p>Obtain an instance via {@link #getInstance()}, which currently returns an {@link
 * AES256EncryptedBytesFactory}.
 */
public interface EncryptedBytesFactory {
  /**
   * Returns the platform's default {@link EncryptedBytesFactory} implementation.
   *
   * @return an {@link AES256EncryptedBytesFactory} instance
   */
  static EncryptedBytesFactory getInstance() {
    return new AES256EncryptedBytesFactory();
  }

  /**
   * Encrypts the given clear bytes and wraps the result.
   *
   * @param clearBytes the unencrypted bytes to encrypt
   * @return an {@link EncryptedBytes} holding the encrypted form of {@code clearBytes}
   */
  EncryptedBytes withClearBytes(byte[] clearBytes);

  /**
   * Wraps already-encrypted bytes without re-encrypting them.
   *
   * @param encryptedBytes the previously encrypted bytes
   * @return an {@link EncryptedBytes} holding {@code encryptedBytes}
   */
  EncryptedBytes withEncryptedBytes(byte[] encryptedBytes);
}
