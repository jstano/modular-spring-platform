package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordFactory;

public interface PasswordFactory {
  static PasswordFactory getInstance() {
    return new SHA256PasswordFactory();
  }

  Password withClearText(String clearText);

  Password withEncryptedText(String encryptedText);
}
