package com.stano.jackson;

import org.apache.commons.lang3.StringUtils;

public class JSON {
  public static <T> T parse(String jsonText, Class<T> dataTypeClass) {
    if (StringUtils.isBlank(jsonText)) {
      return null;
    }

    return ObjectMapperFactory.getInstance().readValue(jsonText, dataTypeClass);
  }

  public static String toString(Object value) {
    return ObjectMapperFactory.getInstance().writeValueAsString(value);
  }
}
