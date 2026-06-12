package com.stano.crypto.binary;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.binary.aes256.AES256EncryptedBytesFactory;
import org.junit.jupiter.api.Test;

class EncryptedBytesFactoryTest {

  @Test
  void getInstanceReturnsDynamicEncryptedBytesFactoryObjectByDefault() {
    assertThat(EncryptedBytesFactory.getInstance().getClass())
        .isEqualTo(AES256EncryptedBytesFactory.class);
  }
}
