package com.stano.crypto.text.aes256;

import com.stano.crypto.text.AbstractEncryptedText;
import com.stano.crypto.text.TextEncryptionServicesFactory;

public final class AES256EncryptedText extends AbstractEncryptedText {
  public AES256EncryptedText(String encryptedText) {
    super(encryptedText);
  }

  @Override
  public String getClearText() {
    return TextEncryptionServicesFactory.getInstance().decryptString(encryptedText);
  }
}
