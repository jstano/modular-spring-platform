package com.stano.crypto.text.aes256;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AES256EncryptedTextTest {

  @Test
  void testBasicStuff() {
    var encryptedText = new AES256TextEncryptionServices().encryptString("abc123");
    var aes256EncryptedText = new AES256EncryptedText(encryptedText);

    assertThat(aes256EncryptedText.getEncryptedText()).isEqualTo(encryptedText);
    assertThat(aes256EncryptedText.getClearText()).isEqualTo("abc123");
  }

  @Test
  void testEquals() {
    var encryptedText1 = new AES256TextEncryptionServices().encryptString("abc123");
    var encryptedText2 = new AES256TextEncryptionServices().encryptString("123abc");

    var aes256EncryptedText1a = new AES256EncryptedText(encryptedText1);
    var aes256EncryptedText1b = new AES256EncryptedText(encryptedText1);
    var aes256EncryptedText2 = new AES256EncryptedText(encryptedText2);

    assertThat(aes256EncryptedText1a).isEqualTo(aes256EncryptedText1a);
    assertThat(aes256EncryptedText1a).isEqualTo(aes256EncryptedText1b);
    assertThat(aes256EncryptedText1a).isNotEqualTo(aes256EncryptedText2);
    assertThat(aes256EncryptedText1a).isNotEqualTo("abc123");
  }

  @Test
  void testHashCode() {
    var encryptedText1 = new AES256TextEncryptionServices().encryptString("abc123");
    var encryptedText2 = new AES256TextEncryptionServices().encryptString("123abc");

    var aes256EncryptedText1a = new AES256EncryptedText(encryptedText1);
    var aes256EncryptedText1b = new AES256EncryptedText(encryptedText1);
    var aes256EncryptedText2 = new AES256EncryptedText(encryptedText2);

    assertThat(aes256EncryptedText1a.hashCode()).isEqualTo(aes256EncryptedText1a.hashCode());
    assertThat(aes256EncryptedText1a.hashCode()).isEqualTo(aes256EncryptedText1b.hashCode());
    assertThat(aes256EncryptedText1a.hashCode()).isNotEqualTo(aes256EncryptedText2.hashCode());
  }

  @Test
  void roundTripEncryptionShouldPreserveTheOriginalText() {
    var plaintext = "Hello World";
    var encryptedText = new AES256TextEncryptionServices().encryptString(plaintext);
    var aes256EncryptedText = new AES256EncryptedText(encryptedText);

    assertThat(aes256EncryptedText.getEncryptedText()).isEqualTo(encryptedText);
    assertThat(aes256EncryptedText.getClearText()).isEqualTo(plaintext);
  }
}
