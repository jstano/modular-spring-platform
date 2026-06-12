package com.stano.crypto.text;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.text.aes256.AES256EncryptedTextFactory;
import org.junit.jupiter.api.Test;

class EncryptedTextFactoryTest {

  @Test
  void getInstanceReturnsDynamicEncryptedTextFactoryObjectByDefault() {
    assertThat(EncryptedTextFactory.getInstance().getClass())
        .isEqualTo(AES256EncryptedTextFactory.class);
  }
}
