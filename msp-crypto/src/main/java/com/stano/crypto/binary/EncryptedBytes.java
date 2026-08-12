package com.stano.crypto.binary;

import java.io.Serializable;

/**
 * A byte array value that carries its encrypted form and can decrypt itself on demand.
 *
 * <p>Instances are typically created via {@link EncryptedBytesFactory}, which pairs the value with
 * an algorithm-specific implementation.
 */
public interface EncryptedBytes extends Serializable {
  /**
   * Returns the encrypted representation of this value.
   *
   * @return the encrypted bytes
   */
  byte[] getEncryptedBytes();

  /**
   * Decrypts and returns the original, unencrypted bytes.
   *
   * @return the decrypted (clear) bytes
   */
  byte[] getClearBytes();
}
