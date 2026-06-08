package com.stano.crypto.binary;

import com.stano.crypto.binary.aes256.AES256BinaryEncryptionServices;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BinaryEncryptionServicesFactoryTest {

    @Test
    void shouldGetInstanceOfAES256BinaryEncryptionServices() {
        assertThat(BinaryEncryptionServicesFactory.getInstance()).isInstanceOf(AES256BinaryEncryptionServices.class);
    }
}
