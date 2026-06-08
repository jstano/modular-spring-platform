package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordFactoryTest {

    @Test
    void getInstanceReturnsDynamicPasswordFactory() {
        assertThat(PasswordFactory.getInstance().getClass()).isEqualTo(SHA256PasswordFactory.class);
    }
}
