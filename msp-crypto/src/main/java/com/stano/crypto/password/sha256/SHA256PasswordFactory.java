package com.stano.crypto.password.sha256;

import com.stano.crypto.password.Password;
import com.stano.crypto.password.PasswordEncryptionServicesFactory;
import com.stano.crypto.password.PasswordFactory;

/**
 * Default implementation of {@link PasswordFactory}.
 *
 * <p>Obtain an instance via {@link PasswordFactory#getInstance()}.
 */
public final class SHA256PasswordFactory implements PasswordFactory {
  /**
   * Hashes the given clear-text password and wraps the result.
   *
   * @param clearText the plain-text password to hash
   * @return a {@link SHA256Password} holding the hashed form of {@code clearText}
   */
  @Override
  public Password withClearText(String clearText) {
    return new SHA256Password(
        PasswordEncryptionServicesFactory.getInstance().encryptPassword(clearText));
  }

  /**
   * Wraps an already-hashed password without re-hashing it.
   *
   * @param encryptedText the previously hashed password
   * @return a {@link SHA256Password} holding {@code encryptedText}
   */
  @Override
  public Password withEncryptedText(String encryptedText) {
    return new SHA256Password(encryptedText);
  }
}
