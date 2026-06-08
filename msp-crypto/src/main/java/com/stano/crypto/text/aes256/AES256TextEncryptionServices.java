package com.stano.crypto.text.aes256;

import com.stano.crypto.text.TextEncryptionServices;
import com.stano.crypto.utils.EncryptionSecretProvider;

public final class AES256TextEncryptionServices implements TextEncryptionServices {
  private static final AES256TextEncryptor textEncryptor = new AES256TextEncryptor(EncryptionSecretProvider.getSecret());

  @Override
  public String encryptString(String clearText) {
    return textEncryptor.encrypt(clearText);
  }

  @Override
  public String decryptString(String encryptedText) {
    return textEncryptor.decrypt(encryptedText);
  }
}
