package com.stano.crypto.password;

public interface PasswordEncryptionServices {
  String encryptPassword(String password);

  boolean passwordMatches(String plainPassword, String encryptedPassword);
}
