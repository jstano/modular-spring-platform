package com.stano.crypto.password;

public abstract class AbstractPassword implements Password {
  protected final String encryptedPassword;

  @Override
  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  protected AbstractPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Password)) {
      return false;
    }

    Password that = (Password)o;

    return encryptedPassword.equals(that.getEncryptedPassword());
  }

  @Override
  public int hashCode() {
    return encryptedPassword.hashCode();
  }
}
