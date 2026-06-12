package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.BinaryEncryptionServicesFactory;
import com.stano.crypto.binary.EncryptedBytes;
import com.stano.crypto.binary.EncryptedBytesFactory;

public final class AES256EncryptedBytesFactory implements EncryptedBytesFactory {
  @Override
  public EncryptedBytes withClearBytes(byte[] clearBytes) {
    return new AES256EncryptedBytes(
        BinaryEncryptionServicesFactory.getInstance().encryptBytes(clearBytes));
  }

  @Override
  public EncryptedBytes withEncryptedBytes(byte[] encryptedBytes) {
    return new AES256EncryptedBytes(encryptedBytes);
  }
}
