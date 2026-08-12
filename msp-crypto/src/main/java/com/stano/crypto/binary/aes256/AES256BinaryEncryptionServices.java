package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.BinaryEncryptionServices;
import com.stano.crypto.utils.EncryptionSecretProvider;

/**
 * AES-256 implementation of {@link BinaryEncryptionServices}.
 *
 * <p>Delegates to a shared {@link AES256BytesEncryptor} keyed with the secret resolved by {@link
 * EncryptionSecretProvider#getSecret()}. Obtain an instance via {@code
 * BinaryEncryptionServicesFactory.getInstance()} rather than constructing directly.
 */
public final class AES256BinaryEncryptionServices implements BinaryEncryptionServices {
  private static final AES256BytesEncryptor binaryEncryptor =
      new AES256BytesEncryptor(EncryptionSecretProvider.getSecret());

  /**
   * Encrypts the given clear bytes using AES-256.
   *
   * @param clearBytes the bytes to encrypt
   * @return the encrypted bytes
   * @throws IllegalArgumentException if encryption fails
   */
  @Override
  public byte[] encryptBytes(byte[] clearBytes) {
    return binaryEncryptor.encrypt(clearBytes);
  }

  /**
   * Decrypts the given encrypted bytes using AES-256.
   *
   * @param encryptedBytes the bytes to decrypt
   * @return the decrypted (clear) bytes
   * @throws IllegalArgumentException if decryption fails
   */
  @Override
  public byte[] decryptBytes(byte[] encryptedBytes) {
    return binaryEncryptor.decrypt(encryptedBytes);
  }
}
