package com.stano.crypto.text.aes256;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AES256EncryptedTextFactoryTest {

    @Test
    void withPlainTextShouldReturnInstanceOfAES256EncryptedTextAndShouldWork() {
        var factory = new AES256EncryptedTextFactory();

        assertThat(factory.withClearText("abc123")).isInstanceOf(AES256EncryptedText.class);
        assertThat(factory.withClearText("abc123").getClearText()).isEqualTo("abc123");
    }

    @Test
    void withEncryptedTextShouldReturnInstanceOfAES256EncryptedTextAndShouldWork() {
        var factory = new AES256EncryptedTextFactory();
        var encryptedText = new AES256TextEncryptionServices().encryptString("abc123");

        assertThat(factory.withEncryptedText(encryptedText)).isInstanceOf(AES256EncryptedText.class);
        assertThat(factory.withEncryptedText(encryptedText).getClearText()).isEqualTo("abc123");
    }
}
