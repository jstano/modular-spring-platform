package com.stano.crypto.password.sha256;

import com.stano.crypto.password.PasswordEncryptionServices;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Default implementation of {@link PasswordEncryptionServices}.
 *
 * <p>Note: despite this class's package name, hashing is delegated to Spring Security's {@link
 * BCryptPasswordEncoder} (bcrypt), not literal SHA-256. Obtain an instance via {@code
 * PasswordEncryptionServicesFactory.getInstance()} rather than constructing directly.
 */
public final class SHA256PasswordEncryptionServices implements PasswordEncryptionServices {
  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * Hashes the given plain-text password using bcrypt.
   *
   * @param password the plain-text password to hash
   * @return the bcrypt-hashed password
   */
  @Override
  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  /**
   * Checks whether a plain-text password matches a previously bcrypt-hashed password.
   *
   * @param plainPassword the candidate plain-text password
   * @param encryptedPassword the bcrypt-hashed password to compare against
   * @return {@code true} if {@code plainPassword} matches {@code encryptedPassword}
   */
  @Override
  public boolean passwordMatches(String plainPassword, String encryptedPassword) {
    return passwordEncoder.matches(plainPassword, encryptedPassword);
  }
}
