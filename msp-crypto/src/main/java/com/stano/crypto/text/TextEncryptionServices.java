package com.stano.crypto.text;

/**
 * Encrypts and decrypts text.
 *
 * <p>Obtain an instance via {@link TextEncryptionServicesFactory#getInstance()}.
 */
public interface TextEncryptionServices {
  /**
   * Encrypts the given clear text.
   *
   * @param clearText the text to encrypt
   * @return the encrypted text
   */
  String encryptString(String clearText);

  /**
   * Decrypts the given encrypted text.
   *
   * @param encryptedText the text to decrypt
   * @return the decrypted (clear) text
   */
  String decryptString(String encryptedText);
}
