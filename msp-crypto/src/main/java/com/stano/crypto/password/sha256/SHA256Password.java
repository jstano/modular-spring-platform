package com.stano.crypto.password.sha256;

import com.stano.crypto.password.AbstractPassword;
import com.stano.crypto.password.PasswordEncryptionServices;

/**
 * Default implementation of {@link com.stano.crypto.password.Password}.
 *
 * <p>Wraps an already-hashed password and verifies candidates via a private {@link
 * SHA256PasswordEncryptionServices} instance, which is bcrypt-backed despite this class's name.
 * Instances are normally created through {@link SHA256PasswordFactory} rather than this constructor
 * directly.
 */
public final class SHA256Password extends AbstractPassword {
  private static final PasswordEncryptionServices encryptionServices =
      new SHA256PasswordEncryptionServices();

  /**
   * Creates an instance wrapping the given already-hashed password.
   *
   * @param encryptedPassword the hashed password to wrap
   */
  public SHA256Password(String encryptedPassword) {
    super(encryptedPassword);
  }

  /**
   * Checks whether the given plain-text password matches this hashed password.
   *
   * @param plainTextPassword the candidate plain-text password
   * @return {@code true} if {@code plainTextPassword} matches this password's hashed value
   */
  @Override
  public boolean matches(String plainTextPassword) {
    return encryptionServices.passwordMatches(plainTextPassword, encryptedPassword);
  }
}
