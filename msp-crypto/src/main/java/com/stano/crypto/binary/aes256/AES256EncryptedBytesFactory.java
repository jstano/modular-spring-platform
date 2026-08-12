package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.BinaryEncryptionServicesFactory;
import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.binary.EncryptedBytesFactory;

/**
 * AES-256 implementation of {@link EncryptedBytesFactory}.
 *
 * <p>Obtain an instance via {@link EncryptedBytesFactory#getInstance()}.
 */
public final class AES256EncryptedBytesFactory implements EncryptedBytesFactory {
  /**
   * Encrypts the given clear bytes using AES-256 and wraps the result.
   *
   * @param clearBytes the unencrypted bytes to encrypt
   * @return an {@link AES256EncryptedBytes} holding the encrypted form of {@code clearBytes}
   * @throws IllegalArgumentException if encryption fails
   */
  @Override
  public EncryptedBytes withClearBytes(byte[] clearBytes) {
    return new AES256EncryptedBytes(
        BinaryEncryptionServicesFactory.getInstance().encryptBytes(clearBytes));
  }

  /**
   * Wraps already AES-256-encrypted bytes without re-encrypting them.
   *
   * @param encryptedBytes the previously encrypted bytes
   * @return an {@link AES256EncryptedBytes} holding {@code encryptedBytes}
   */
  @Override
  public EncryptedBytes withEncryptedBytes(byte[] encryptedBytes) {
    return new AES256EncryptedBytes(encryptedBytes);
  }
}
