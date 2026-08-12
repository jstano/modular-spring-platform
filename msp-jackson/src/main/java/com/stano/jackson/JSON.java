package com.stano.jackson;

import org.apache.commons.lang3.StringUtils;

/**
 * Convenience helper for converting between JSON strings and Java objects using the platform's
 * shared {@link ObjectMapperFactory} instance.
 */
public class JSON {
  /**
   * Parses the given JSON text into an instance of the given type.
   *
   * @param jsonText the JSON text to parse; if blank, {@code null} is returned without parsing
   * @param dataTypeClass the target type to deserialize into
   * @param <T> the target type
   * @return the deserialized instance, or {@code null} if {@code jsonText} is blank
   */
  public static <T> T parse(String jsonText, Class<T> dataTypeClass) {
    if (StringUtils.isBlank(jsonText)) {
      return null;
    }

    return ObjectMapperFactory.getInstance().readValue(jsonText, dataTypeClass);
  }

  /**
   * Serializes the given value to its JSON string representation.
   *
   * @param value the value to serialize
   * @return the JSON string representation of {@code value}
   */
  public static String toString(Object value) {
    return ObjectMapperFactory.getInstance().writeValueAsString(value);
  }
}
