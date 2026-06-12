package com.stano.crypto.binary;

public interface BinaryEncryptionServices {
  byte[] encryptBytes(byte[] clearBytes);

  byte[] decryptBytes(byte[] encryptedBytes);
}
