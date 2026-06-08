package com.stano.crypto.password.sha256;

import com.stano.crypto.password.Password;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SHA256PasswordTest {

    @Test
    void matchesReturnsTrueIfPasswordMatches() {
        assertThat(new SHA256PasswordFactory().withClearText("abc123").matches("abc123")).isTrue();
    }

    @Test
    void matchesReturnsFalseIfPasswordDoNotMatch() {
        assertThat(new SHA256PasswordFactory().withClearText("abc123").matches("123abc")).isFalse();
    }

    @Test
    void getEncryptedPasswordReturnsWhateverStringIsPassedInToTheConstructor() {
        assertThat(new SHA256Password("abc123").getEncryptedPassword()).isEqualTo("abc123");
    }

    @Test
    void testEquals() {
        var password1 = new SHA256Password("abc123");
        var password2 = new SHA256Password("abc123");
        var password3 = new SHA256Password("123abc");

        assertThat(password1).isEqualTo(password1);
        assertThat(password1).isEqualTo(password2);
        assertThat(password1).isNotEqualTo(password3);
        assertThat(password3).isNotEqualTo(password1);
        assertThat(password1).isNotEqualTo(null);
    }

    @Test
    void testHashCode() {
        var password1 = new SHA256Password("abc123");
        var password2 = new SHA256Password("abc123");
        var password3 = new SHA256Password("123abc");

        Map<Password, Password> passwordMap = new HashMap<>();
        passwordMap.put(password1, password1);
        passwordMap.put(password2, password2);
        passwordMap.put(password3, password3);

        assertThat(passwordMap).hasSize(2);
        assertThat(passwordMap.get(password1)).isEqualTo(password2);
        assertThat(passwordMap.get(password3)).isEqualTo(password3);
    }
}
