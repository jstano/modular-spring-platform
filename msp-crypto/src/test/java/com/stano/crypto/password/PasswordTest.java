package com.stano.crypto.password;

import com.stano.crypto.password.sha256.SHA256Password;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @Test
    void creationOfSHA256PasswordWithTextReturnsSameTextFromTheEncryptedPasswordMethod() {
        var password = new SHA256Password("abc123");

        assertThat(password.getEncryptedPassword()).isEqualTo("abc123");
    }
}
