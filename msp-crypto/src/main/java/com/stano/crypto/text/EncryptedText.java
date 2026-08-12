package com.stano.crypto.text;

import java.io.Serializable;

/**
 * A text value that carries its encrypted form and can decrypt itself on demand.
 *
 * <p>Instances are typically created via {@link EncryptedTextFactory}, which pairs the value with
 * an algorithm-specific implementation.
 */
public interface EncryptedText extends Serializable {
  /**
   * Returns the encrypted representation of this value.
   *
   * @return the encrypted text
   */
  String getEncryptedText();

  /**
   * Decrypts and returns the original, unencrypted text.
   *
   * @return the decrypted (clear) text
   */
  String getClearText();
}
