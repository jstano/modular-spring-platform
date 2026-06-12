package com.stano.crypto.text;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.text.aes256.AES256TextEncryptionServices;
import org.junit.jupiter.api.Test;

class TextEncryptionServicesFactoryTest {

  @Test
  void shouldGetTheCorrectTypeForEachMode() {
    assertThat(TextEncryptionServicesFactory.getInstance())
        .isInstanceOf(AES256TextEncryptionServices.class);
  }
}
