package com.stano.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

public final class ObjectMapperFactory {
  public static JsonMapper.Builder configure(JsonMapper.Builder builder) {
    return builder
        .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        .enable(MapperFeature.DEFAULT_VIEW_INCLUSION)
        .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
        .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
        .changeDefaultPropertyInclusion(
            incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
        .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED);
  }

  private static final class Holder {
    static final ObjectMapper INSTANCE =
        configure(JsonMapper.builder())
            .findAndAddModules(ObjectMapperFactory.class.getClassLoader())
            .build();
  }

  public static ObjectMapper getInstance() {
    return Holder.INSTANCE;
  }

  private ObjectMapperFactory() {}
}
