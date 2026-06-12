package com.stano.crypto.password.sha256;

import com.stano.crypto.password.Password;
import com.stano.crypto.password.PasswordEncryptionServicesFactory;
import com.stano.crypto.password.PasswordFactory;

public final class SHA256PasswordFactory implements PasswordFactory {
  @Override
  public Password withClearText(String clearText) {
    return new SHA256Password(
        PasswordEncryptionServicesFactory.getInstance().encryptPassword(clearText));
  }

  @Override
  public Password withEncryptedText(String encryptedText) {
    return new SHA256Password(encryptedText);
  }
}
