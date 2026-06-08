package com.stano.crypto.binary;

import java.io.Serializable;

public interface EncryptedBytes extends Serializable {
  byte[] getEncryptedBytes();

  byte[] getClearBytes();
}
