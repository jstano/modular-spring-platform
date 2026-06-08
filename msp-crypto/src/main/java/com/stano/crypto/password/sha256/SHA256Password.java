package com.stano.crypto.password.sha256;

import com.stano.crypto.password.PasswordEncryptionServices;
import com.stano.crypto.password.AbstractPassword;

public final class SHA256Password extends AbstractPassword {
  private static final PasswordEncryptionServices encryptionServices = new SHA256PasswordEncryptionServices();

  public SHA256Password(String encryptedPassword) {
    super(encryptedPassword);
  }

  @Override
  public boolean matches(String plainTextPassword) {
    return encryptionServices.passwordMatches(plainTextPassword, encryptedPassword);
  }
}
