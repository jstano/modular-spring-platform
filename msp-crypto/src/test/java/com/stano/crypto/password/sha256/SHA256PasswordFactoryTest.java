package com.stano.crypto.password.sha256;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SHA256PasswordFactoryTest {

    @Test
    void withClearTextShouldReturnSHA256PasswordAndItShouldWork() {
        assertThat(new SHA256PasswordFactory().withClearText("abc123").matches("abc123")).isTrue();
    }

    @Test
    void withEncryptedTextShouldReturnSHA256PasswordAndItShouldWork() {
        var encryptedPassword = new SHA256PasswordEncryptionServices().encryptPassword("abc123");
        assertThat(new SHA256PasswordFactory().withEncryptedText(encryptedPassword).matches("abc123")).isTrue();
    }
}
