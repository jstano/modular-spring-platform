package com.stano.crypto.utils;

public final class EncryptionSecretProvider {
  public static String getSecret() {
    String secret = System.getProperty("msp.encryption.secret");
    if (secret == null) {
      throw new IllegalStateException(
          "System property 'msp.encryption.secret' is not set. " +
          "Start the application with -Dmsp.encryption.secret=<your-secret>.");
    }
    return secret;
  }

  private EncryptionSecretProvider() {
  }
}
