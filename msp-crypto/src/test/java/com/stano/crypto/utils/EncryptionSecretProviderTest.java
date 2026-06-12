package com.stano.crypto.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EncryptionSecretProviderTest {

  @Test
  void shouldThrowWhenSecretIsNotSet() {
    assertThatThrownBy(EncryptionSecretProvider::getSecret)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("msp.encryption.secret");
  }

  @Test
  void shouldUseTheConfiguredSecret() {
    System.setProperty("msp.encryption.secret", "the-secret");

    try {
      assertThat(EncryptionSecretProvider.getSecret()).isEqualTo("the-secret");
    } finally {
      System.clearProperty("msp.encryption.secret");
    }
  }
}
