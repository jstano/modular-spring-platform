package com.stano.crypto.utils;

import java.io.UnsupportedEncodingException;
import java.util.function.Function;
import org.apache.commons.codec.binary.Base64;

/**
 * This class is used to encapsulate the base64 encoding/decoding used by the encryption algorithms.
 * The input String is converted into bytes using MESSAGE_CHARSET as a fixed charset to avoid
 * problems with different platforms having different default charsets (see MESSAGE_CHARSET doc).
 */
public class Base64Utils {
  private static final String MESSAGE_CHARSET = "UTF-8";
  private static final String ENCRYPTED_MESSAGE_CHARSET = "US-ASCII";

  private static final Base64 base64 = new Base64(Integer.MAX_VALUE, new byte[0], true);

  /**
   * Encodes a message by applying the given transform (typically an encryption function) to its
   * UTF-8 bytes and Base64-encoding the result.
   *
   * @param message the plain-text message to encode
   * @param f the transform applied to the message's UTF-8 bytes before Base64 encoding
   * @return the Base64-encoded, transformed message
   * @throws IllegalArgumentException if the message cannot be encoded
   */
  public static String encode(String message, Function<byte[], byte[]> f) {
    try {
      final byte[] messageBytes = message.getBytes(MESSAGE_CHARSET);
      final byte[] encryptedMessageBytes = base64.encode(f.apply(messageBytes));

      return new String(encryptedMessageBytes, ENCRYPTED_MESSAGE_CHARSET);
    } catch (UnsupportedEncodingException x) {
      throw new IllegalArgumentException("Failed to encode message", x);
    }
  }

  /**
   * Decodes a Base64-encoded message and applies the given transform (typically a decryption
   * function) to the decoded bytes, returning the result as a UTF-8 string.
   *
   * @param encryptedMessage the Base64-encoded message to decode
   * @param f the transform applied to the Base64-decoded bytes
   * @return the transformed, decoded message
   * @throws IllegalArgumentException if the message cannot be decoded
   */
  public static String decode(String encryptedMessage, Function<byte[], byte[]> f) {
    try {
      byte[] encryptedMessageBytes =
          base64.decode(encryptedMessage.getBytes(ENCRYPTED_MESSAGE_CHARSET));

      return new String(f.apply(encryptedMessageBytes), MESSAGE_CHARSET);
    } catch (UnsupportedEncodingException x) {
      throw new IllegalArgumentException("Failed to decode message", x);
    }
  }
}
