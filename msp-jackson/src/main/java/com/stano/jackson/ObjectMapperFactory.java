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

/**
 * Factory for the platform's singleton, preconfigured Jackson {@link ObjectMapper}.
 *
 * <p>The shared instance is lazily created on first access (via the classic
 * initialization-on-demand holder idiom) and configured with the platform's standard
 * (de)serialization settings: single-value-as-array acceptance, default view inclusion, plain-text
 * {@code BigDecimal} writing, lenient handling of empty beans and unknown properties, ISO-8601
 * date/time formatting, {@code NON_NULL} property inclusion, and properties-based constructor
 * detection. All Jackson modules available on the classpath are auto-registered.
 */
public final class ObjectMapperFactory {
  /**
   * Applies the platform's standard {@code ObjectMapper} settings to the given builder. Exposed
   * separately from {@link #getInstance()} so callers who need a differently configured mapper (for
   * example, with additional modules) can start from the same baseline.
   *
   * @param builder the builder to configure
   * @return the same builder, with the platform's standard settings applied
   */
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

  /**
   * Returns the platform's shared, preconfigured {@code ObjectMapper} instance.
   *
   * @return the singleton {@code ObjectMapper}
   */
  public static ObjectMapper getInstance() {
    return Holder.INSTANCE;
  }

  private ObjectMapperFactory() {}
}
