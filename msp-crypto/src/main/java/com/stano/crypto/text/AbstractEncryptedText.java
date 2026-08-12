package com.stano.crypto.text;

/**
 * Base class for {@link EncryptedText} implementations.
 *
 * <p>Stores the encrypted text and implements {@link #equals(Object)} and {@link #hashCode()} based
 * on its contents; subclasses supply {@link #getClearText()}.
 */
public abstract class AbstractEncryptedText implements EncryptedText {
  protected final String encryptedText;

  /**
   * Creates an instance wrapping the given encrypted text.
   *
   * @param encryptedText the encrypted text to wrap
   */
  public AbstractEncryptedText(String encryptedText) {
    this.encryptedText = encryptedText;
  }

  /**
   * Returns the encrypted representation of this value.
   *
   * @return the encrypted text
   */
  @Override
  public String getEncryptedText() {
    return encryptedText;
  }

  /**
   * Compares this instance to another based on the encrypted text, requiring {@code o} to also be
   * an {@link AbstractEncryptedText}.
   *
   * @param o the object to compare against
   * @return {@code true} if {@code o} is an {@link AbstractEncryptedText} with equal encrypted text
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractEncryptedText)) {
      return false;
    }

    AbstractEncryptedText that = (AbstractEncryptedText) o;

    return encryptedText.equals(that.encryptedText);
  }

  /**
   * Returns a hash code derived from the encrypted text.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return encryptedText.hashCode();
  }
}
