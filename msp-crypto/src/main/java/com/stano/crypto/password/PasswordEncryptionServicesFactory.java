package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordEncryptionServices;

/**
 * Factory for obtaining the platform's configured {@link PasswordEncryptionServices}
 * implementation.
 *
 * <p>This is a static factory, not a constructible class; use {@link #getInstance()} to obtain the
 * shared {@link SHA256PasswordEncryptionServices} instance.
 */
public final class PasswordEncryptionServicesFactory {
  private static final SHA256PasswordEncryptionServices sha256PasswordEncryptionServices =
      new SHA256PasswordEncryptionServices();

  /**
   * Returns the platform's default {@link PasswordEncryptionServices} implementation.
   *
   * @return a {@link SHA256PasswordEncryptionServices} instance
   */
  public static PasswordEncryptionServices getInstance() {
    return sha256PasswordEncryptionServices;
  }

  private PasswordEncryptionServicesFactory() {}
}
