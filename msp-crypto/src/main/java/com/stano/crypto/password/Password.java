package com.stano.crypto.password;

import java.io.Serializable;

/**
 * A one-way hashed password that can verify a plain-text candidate against its hashed form.
 *
 * <p>Instances are typically created via {@link PasswordFactory}, which pairs the value with an
 * algorithm-specific implementation.
 */
public interface Password extends Serializable {
  /**
   * Returns the hashed (encrypted) form of this password.
   *
   * @return the encrypted password
   */
  String getEncryptedPassword();

  /**
   * Checks whether the given plain-text password matches this hashed password.
   *
   * @param plainTextPassword the candidate plain-text password
   * @return {@code true} if {@code plainTextPassword} matches this password's hashed value
   */
  boolean matches(String plainTextPassword);
}
