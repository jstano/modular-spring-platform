package com.stano.crypto.password;

import java.io.Serializable;

public interface Password extends Serializable {
  String getEncryptedPassword();

  boolean matches(String plainTextPassword);
}
