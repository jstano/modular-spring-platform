package com.stano.crypto.text;

import com.stano.crypto.text.aes256.AES256EncryptedTextFactory;

public interface EncryptedTextFactory {
  static EncryptedTextFactory getInstance() {
    return new AES256EncryptedTextFactory();
  }

  EncryptedText withClearText(String clearText);

  EncryptedText withEncryptedText(String encryptedText);
}
