package com.stano.crypto.binary;

/**
 * Encrypts and decrypts raw byte arrays.
 *
 * <p>Obtain an instance via {@link BinaryEncryptionServicesFactory#getInstance()}.
 */
public interface BinaryEncryptionServices {
  /**
   * Encrypts the given clear bytes.
   *
   * @param clearBytes the bytes to encrypt
   * @return the encrypted bytes
   */
  byte[] encryptBytes(byte[] clearBytes);

  /**
   * Decrypts the given encrypted bytes.
   *
   * @param encryptedBytes the bytes to decrypt
   * @return the decrypted (clear) bytes
   */
  byte[] decryptBytes(byte[] encryptedBytes);
}
