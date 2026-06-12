package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordEncryptionServices;

public final class PasswordEncryptionServicesFactory {
  private static final SHA256PasswordEncryptionServices sha256PasswordEncryptionServices =
      new SHA256PasswordEncryptionServices();

  public static PasswordEncryptionServices getInstance() {
    return sha256PasswordEncryptionServices;
  }

  private PasswordEncryptionServicesFactory() {}
}
