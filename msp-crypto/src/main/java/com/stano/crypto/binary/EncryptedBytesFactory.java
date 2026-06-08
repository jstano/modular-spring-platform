package com.stano.crypto.binary;

import com.stano.crypto.binary.aes256.AES256EncryptedBytesFactory;

public interface EncryptedBytesFactory {
  static EncryptedBytesFactory getInstance() {
    return new AES256EncryptedBytesFactory();
  }

  EncryptedBytes withClearBytes(byte[] clearBytes);

  EncryptedBytes withEncryptedBytes(byte[] encryptedBytes);
}
