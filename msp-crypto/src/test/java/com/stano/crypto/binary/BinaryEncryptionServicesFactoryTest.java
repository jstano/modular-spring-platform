package com.stano.crypto.binary;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.binary.aes256.AES256BinaryEncryptionServices;
import org.junit.jupiter.api.Test;

class BinaryEncryptionServicesFactoryTest {

  @Test
  void shouldGetInstanceOfAES256BinaryEncryptionServices() {
    assertThat(BinaryEncryptionServicesFactory.getInstance())
        .isInstanceOf(AES256BinaryEncryptionServices.class);
  }
}
