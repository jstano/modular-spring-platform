package com.stano.crypto.text;

public interface TextEncryptionServices {
  String encryptString(String clearText);

  String decryptString(String encryptedText);
}
