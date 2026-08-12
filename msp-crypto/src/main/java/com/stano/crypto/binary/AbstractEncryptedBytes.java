package com.stano.crypto.binary;

import java.util.Arrays;

/**
 * Base class for {@link EncryptedBytes} implementations.
 *
 * <p>Stores the encrypted byte array and implements {@link #equals(Object)} and {@link #hashCode()}
 * based on its contents; subclasses supply {@link #getClearBytes()}.
 */
public abstract class AbstractEncryptedBytes implements EncryptedBytes {
  protected final byte[] encryptedBytes;

  /**
   * Creates an instance wrapping the given encrypted bytes.
   *
   * @param encryptedBytes the encrypted bytes to wrap
   */
  public AbstractEncryptedBytes(byte[] encryptedBytes) {
    this.encryptedBytes = encryptedBytes;
  }

  /**
   * Returns the encrypted representation of this value.
   *
   * @return the encrypted bytes
   */
  @Override
  public byte[] getEncryptedBytes() {
    return encryptedBytes;
  }

  /**
   * Compares this instance to another based on the encrypted byte array contents and exact runtime
   * type.
   *
   * @param o the object to compare against
   * @return {@code true} if {@code o} is the same class and holds an equal encrypted byte array
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEncryptedBytes that = (AbstractEncryptedBytes) o;
    return Arrays.equals(encryptedBytes, that.encryptedBytes);
  }

  /**
   * Returns a hash code derived from the encrypted byte array contents.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(encryptedBytes);
  }
}
