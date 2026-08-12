package com.stano.crypto.password;

/**
 * Hashes passwords and verifies plain-text candidates against a hashed value.
 *
 * <p>Obtain an instance via {@link PasswordEncryptionServicesFactory#getInstance()}.
 */
public interface PasswordEncryptionServices {
  /**
   * Hashes the given plain-text password.
   *
   * @param password the plain-text password to hash
   * @return the hashed password
   */
  String encryptPassword(String password);

  /**
   * Checks whether a plain-text password matches a previously hashed password.
   *
   * @param plainPassword the candidate plain-text password
   * @param encryptedPassword the hashed password to compare against
   * @return {@code true} if {@code plainPassword} matches {@code encryptedPassword}
   */
  boolean passwordMatches(String plainPassword, String encryptedPassword);
}
