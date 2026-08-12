package com.stano.crypto.password;

/**
 * Base class for {@link Password} implementations.
 *
 * <p>Stores the hashed password and implements {@link #equals(Object)} and {@link #hashCode()}
 * based on its contents; subclasses supply {@link #matches(String)}.
 */
public abstract class AbstractPassword implements Password {
  protected final String encryptedPassword;

  /**
   * Returns the hashed (encrypted) form of this password.
   *
   * @return the encrypted password
   */
  @Override
  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  /**
   * Creates an instance wrapping the given hashed password.
   *
   * @param encryptedPassword the hashed password to wrap
   */
  protected AbstractPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  /**
   * Compares this instance to another based on the hashed password value, requiring {@code o} to
   * also implement {@link Password}.
   *
   * @param o the object to compare against
   * @return {@code true} if {@code o} is a {@link Password} with an equal encrypted password
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Password)) {
      return false;
    }

    Password that = (Password) o;

    return encryptedPassword.equals(that.getEncryptedPassword());
  }

  /**
   * Returns a hash code derived from the hashed password value.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return encryptedPassword.hashCode();
  }
}
