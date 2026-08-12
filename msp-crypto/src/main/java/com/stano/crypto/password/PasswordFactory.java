package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordFactory;

/**
 * Creates {@link Password} instances from clear-text or already-hashed passwords.
 *
 * <p>Obtain an instance via {@link #getInstance()}, which currently returns a {@link
 * SHA256PasswordFactory}.
 */
public interface PasswordFactory {
  /**
   * Returns the platform's default {@link PasswordFactory} implementation.
   *
   * @return a {@link SHA256PasswordFactory} instance
   */
  static PasswordFactory getInstance() {
    return new SHA256PasswordFactory();
  }

  /**
   * Hashes the given clear-text password and wraps the result.
   *
   * @param clearText the plain-text password to hash
   * @return a {@link Password} holding the hashed form of {@code clearText}
   */
  Password withClearText(String clearText);

  /**
   * Wraps an already-hashed password without re-hashing it.
   *
   * @param encryptedText the previously hashed password
   * @return a {@link Password} holding {@code encryptedText}
   */
  Password withEncryptedText(String encryptedText);
}
