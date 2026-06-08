package com.stano.crypto.text;

import java.io.Serializable;

public interface EncryptedText extends Serializable {
  String getEncryptedText();

  String getClearText();
}
