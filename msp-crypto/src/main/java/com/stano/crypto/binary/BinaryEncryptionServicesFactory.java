package com.stano.crypto.binary;

import com.stano.crypto.binary.aes256.AES256BinaryEncryptionServices;

public final class BinaryEncryptionServicesFactory {
  private static final AES256BinaryEncryptionServices aes256TextEncryptionServices =
      new AES256BinaryEncryptionServices();

  public static BinaryEncryptionServices getInstance() {
    return aes256TextEncryptionServices;
  }

  private BinaryEncryptionServicesFactory() {}
}
