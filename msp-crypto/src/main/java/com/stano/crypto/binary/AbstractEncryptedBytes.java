package com.stano.crypto.binary;

import java.util.Arrays;

public abstract class AbstractEncryptedBytes implements EncryptedBytes {
  protected final byte[] encryptedBytes;

  public AbstractEncryptedBytes(byte[] encryptedBytes) {
    this.encryptedBytes = encryptedBytes;
  }

  @Override
  public byte[] getEncryptedBytes() {
    return encryptedBytes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEncryptedBytes that = (AbstractEncryptedBytes)o;
    return Arrays.equals(encryptedBytes, that.encryptedBytes);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(encryptedBytes);
  }
}
