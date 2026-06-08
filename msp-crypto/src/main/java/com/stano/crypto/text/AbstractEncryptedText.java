package com.stano.crypto.text;

public abstract class AbstractEncryptedText implements EncryptedText {
  protected final String encryptedText;

  public AbstractEncryptedText(String encryptedText) {
    this.encryptedText = encryptedText;
  }

  @Override
  public String getEncryptedText() {
    return encryptedText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractEncryptedText)) {
      return false;
    }

    AbstractEncryptedText that = (AbstractEncryptedText)o;

    return encryptedText.equals(that.encryptedText);
  }

  @Override
  public int hashCode() {
    return encryptedText.hashCode();
  }
}
