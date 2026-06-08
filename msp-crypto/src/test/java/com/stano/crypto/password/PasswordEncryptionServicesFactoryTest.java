package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256PasswordEncryptionServices;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncryptionServicesFactoryTest {

    @Test
    void shouldGetTheCorrectTypeForEachMode() {
        assertThat(PasswordEncryptionServicesFactory.getInstance()).isInstanceOf(SHA256PasswordEncryptionServices.class);
    }
}
