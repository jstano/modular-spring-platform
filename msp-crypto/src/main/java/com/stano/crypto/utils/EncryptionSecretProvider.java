package com.stano.crypto.utils;

/**
 * Resolves the shared secret used to key the platform's encryption services.
 *
 * <p>The secret is read from the {@code msp.encryption.secret} system property; use {@link
 * #getSecret()} to retrieve it.
 */
public final class EncryptionSecretProvider {
  /**
   * Returns the encryption secret configured via the {@code msp.encryption.secret} system property.
   *
   * @return the configured encryption secret
   * @throws IllegalStateException if the {@code msp.encryption.secret} system property is not set
   */
  public static String getSecret() {
    String secret = System.getProperty("msp.encryption.secret");
    if (secret == null) {
      throw new IllegalStateException(
          "System property 'msp.encryption.secret' is not set. "
              + "Start the application with -Dmsp.encryption.secret=<your-secret>.");
    }
    return secret;
  }

  private EncryptionSecretProvider() {}
}
