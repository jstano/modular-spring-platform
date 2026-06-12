package com.stano.crypto.password;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.password.sha256.SHA256PasswordEncryptionServices;
import org.junit.jupiter.api.Test;

class PasswordEncryptionServicesFactoryTest {

  @Test
  void shouldGetTheCorrectTypeForEachMode() {
    assertThat(PasswordEncryptionServicesFactory.getInstance())
        .isInstanceOf(SHA256PasswordEncryptionServices.class);
  }
}
