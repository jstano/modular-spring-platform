package com.stano.crypto.binary.aes256;

import com.stano.crypto.binary.BinaryEncryptionServices;
import com.stano.crypto.utils.EncryptionSecretProvider;

public final class AES256BinaryEncryptionServices implements BinaryEncryptionServices {
  private static final AES256BytesEncryptor binaryEncryptor =
      new AES256BytesEncryptor(EncryptionSecretProvider.getSecret());

  @Override
  public byte[] encryptBytes(byte[] clearBytes) {
    return binaryEncryptor.encrypt(clearBytes);
  }

  @Override
  public byte[] decryptBytes(byte[] encryptedBytes) {
    return binaryEncryptor.decrypt(encryptedBytes);
  }
}
