package com.stano.crypto.text;

import com.stano.crypto.text.aes256.AES256EncryptedTextFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptedTextFactoryTest {

    @Test
    void getInstanceReturnsDynamicEncryptedTextFactoryObjectByDefault() {
        assertThat(EncryptedTextFactory.getInstance().getClass()).isEqualTo(AES256EncryptedTextFactory.class);
    }
}
