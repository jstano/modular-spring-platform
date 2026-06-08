package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.AbstractEncryptedBytes;
import com.stano.crypto.binary.BinaryEncryptionServicesFactory;

public final class AES256EncryptedBytes extends AbstractEncryptedBytes {
  public AES256EncryptedBytes(byte[] encryptedBytes) {
    super(encryptedBytes);
  }

  @Override
  public byte[] getClearBytes() {
    return BinaryEncryptionServicesFactory.getInstance().decryptBytes(encryptedBytes);
  }
}
