package com.stano.crypto.password;

import static org.assertj.core.api.Assertions.assertThat;

import com.stano.crypto.password.sha256.SHA256PasswordFactory;
import org.junit.jupiter.api.Test;

class PasswordFactoryTest {

  @Test
  void getInstanceReturnsDynamicPasswordFactory() {
    assertThat(PasswordFactory.getInstance().getClass()).isEqualTo(SHA256PasswordFactory.class);
  }
}
