package com.stano.crypto.text.aes256;

import com.stano.crypto.text.EncryptedText;
import com.stano.crypto.text.EncryptedTextFactory;
import com.stano.crypto.text.TextEncryptionServicesFactory;

public final class AES256EncryptedTextFactory implements EncryptedTextFactory {
  @Override
  public EncryptedText withClearText(String clearText) {
    return new AES256EncryptedText(
        TextEncryptionServicesFactory.getInstance().encryptString(clearText));
  }

  @Override
  public EncryptedText withEncryptedText(String encryptedText) {
    return new AES256EncryptedText(encryptedText);
  }
}
