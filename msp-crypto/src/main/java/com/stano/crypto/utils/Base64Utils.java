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

  public static String encode(String message, Function<byte[], byte[]> f) {
    try {
      final byte[] messageBytes = message.getBytes(MESSAGE_CHARSET);
      final byte[] encryptedMessageBytes = base64.encode(f.apply(messageBytes));

      return new String(encryptedMessageBytes, ENCRYPTED_MESSAGE_CHARSET);
    } catch (UnsupportedEncodingException x) {
      throw new IllegalArgumentException("Failed to encode message", x);
    }
  }

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
