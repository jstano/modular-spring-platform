package com.stano.crypto.text.aes256;

import com.stano.crypto.text.AbstractEncryptedText;
import com.stano.crypto.text.TextEncryptionServicesFactory;

/**
 * AES-256 implementation of {@link com.stano.crypto.text.EncryptedText}.
 *
 * <p>Wraps an already-encrypted string and decrypts it lazily on demand via {@link
 * TextEncryptionServicesFactory}. Instances are normally created through {@link
 * AES256EncryptedTextFactory} rather than this constructor directly.
 */
public final class AES256EncryptedText extends AbstractEncryptedText {
  /**
   * Creates an instance wrapping the given already-encrypted text.
   *
   * @param encryptedText the encrypted text to wrap
   */
  public AES256EncryptedText(String encryptedText) {
    super(encryptedText);
  }

  /**
   * Decrypts and returns the original, unencrypted text using the platform's AES-256 text
   * encryption services.
   *
   * @return the decrypted (clear) text
   * @throws IllegalArgumentException if decryption fails
   */
  @Override
  public String getClearText() {
    return TextEncryptionServicesFactory.getInstance().decryptString(encryptedText);
  }
}
