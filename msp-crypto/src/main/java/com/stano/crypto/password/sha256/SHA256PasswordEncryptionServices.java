package com.stano.crypto.password.sha256;

import com.stano.crypto.password.PasswordEncryptionServices;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class SHA256PasswordEncryptionServices implements PasswordEncryptionServices {
  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public boolean passwordMatches(String plainPassword, String encryptedPassword) {
    return passwordEncoder.matches(plainPassword, encryptedPassword);
  }
}
