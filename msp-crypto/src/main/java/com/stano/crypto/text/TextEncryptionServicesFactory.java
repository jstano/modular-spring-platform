package com.stano.crypto.text;

import com.stano.crypto.text.aes256.AES256TextEncryptionServices;

public final class TextEncryptionServicesFactory {
  private static final AES256TextEncryptionServices aes256TextEncryptionServices = new AES256TextEncryptionServices();

  public static TextEncryptionServices getInstance() {
    return aes256TextEncryptionServices;
  }

  private TextEncryptionServicesFactory() {
  }
}
