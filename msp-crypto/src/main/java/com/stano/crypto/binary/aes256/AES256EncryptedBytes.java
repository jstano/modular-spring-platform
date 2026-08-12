package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.AbstractEncryptedBytes;
import com.stano.crypto.binary.BinaryEncryptionServicesFactory;

/**
 * AES-256 implementation of {@link com.stano.crypto.binary.EncryptedBytes}.
 *
 * <p>Wraps an already-encrypted byte array and decrypts it lazily on demand via {@link
 * BinaryEncryptionServicesFactory}. Instances are normally created through {@link
 * AES256EncryptedBytesFactory} rather than this constructor directly.
 */
public final class AES256EncryptedBytes extends AbstractEncryptedBytes {
  /**
   * Creates an instance wrapping the given already-encrypted bytes.
   *
   * @param encryptedBytes the encrypted bytes to wrap
   */
  public AES256EncryptedBytes(byte[] encryptedBytes) {
    super(encryptedBytes);
  }

  /**
   * Decrypts and returns the original, unencrypted bytes using the platform's AES-256 binary
   * encryption services.
   *
   * @return the decrypted (clear) bytes
   * @throws IllegalArgumentException if decryption fails
   */
  @Override
  public byte[] getClearBytes() {
    return BinaryEncryptionServicesFactory.getInstance().decryptBytes(encryptedBytes);
  }
}
