package com.stano.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class ObjectMapperFactoryTest {

  private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

  @Test
  void convertingAnObjectToAJsonStringAndThenBackToAnObjectShouldWork() throws Exception {
    var localDate = LocalDate.now();
    var localDateTime = LocalDateTime.now();
    var localTime = LocalTime.now();

    var testDTO = new TestDTO();
    testDTO.setId(101);
    testDTO.setName("name");
    testDTO.setLocalDate(localDate);
    testDTO.setLocalDateTime(localDateTime);
    testDTO.setLocalTime(localTime);
    testDTO.setOptionalInt(OptionalInt.of(234));

    var jsonStr = objectMapper.writeValueAsString(testDTO);
    var jsonMap = objectMapper.readValue(jsonStr, Map.class);
    var resultDTO = objectMapper.readValue(jsonStr, TestDTO.class);

    assertThat(String.class.isAssignableFrom(jsonMap.get("localDate").getClass())).isTrue();
    assertThat(String.class.isAssignableFrom(jsonMap.get("localDateTime").getClass())).isTrue();
    assertThat(String.class.isAssignableFrom(jsonMap.get("localTime").getClass())).isTrue();
    assertThat(resultDTO.getId()).isEqualTo(testDTO.getId());
    assertThat(resultDTO.getName()).isEqualTo(testDTO.getName());
    assertThat(resultDTO.getLocalDate()).isEqualTo(testDTO.getLocalDate());
    assertThat(resultDTO.getLocalDateTime()).isEqualTo(testDTO.getLocalDateTime());
    assertThat(resultDTO.getLocalTime()).isEqualTo(testDTO.getLocalTime());
    assertThat(resultDTO.getOptionalInt().getAsInt()).isEqualTo(234);
  }

  @Test
  void nullValuesShouldBeExcludedFromTheJsonString() throws Exception {
    var simpleDTO = new SimpleDTO();
    simpleDTO.setId(101);
    simpleDTO.setName(null);

    var jsonStr = objectMapper.writeValueAsString(simpleDTO);
    var json = objectMapper.readValue(jsonStr, SimpleDTO.class);

    assertThat(jsonStr).isEqualTo("{\"id\":101}");
    assertThat(json.getId()).isEqualTo(101);
    assertThat(json.getName()).isNull();
  }

  @Test
  void mapEntriesWithANullValueShouldBeIncludedInTheOutput() throws Exception {
    Map<String, Object> map = new HashMap<>();
    map.put("a", "aaa");
    map.put("b", "\"bbb\"");
    map.put("c", null);
    map.put("d", false);
    map.put("e", true);
    map.put("f", 0);
    map.put("g", 10);
    map.put("h", "");

    var json = objectMapper.writeValueAsString(map);
    var map2 = objectMapper.readValue(json, HashMap.class);

    assertThat(json)
        .isEqualTo(
            "{\"a\":\"aaa\",\"b\":\"\\\"bbb\\\"\",\"c\":null,\"d\":false,\"e\":true,\"f\":0,\"g\":10,\"h\":\"\"}");

    assertThat(map2.get("a")).isEqualTo("aaa");
    assertThat(map2.get("b")).isEqualTo("\"bbb\"");
    assertThat(map2.get("c")).isNull();
    assertThat(map2.get("d")).isEqualTo(false);
    assertThat(map2.get("e")).isEqualTo(true);
    assertThat(map2.get("f")).isEqualTo(0);
    assertThat(map2.get("g")).isEqualTo(10);
    assertThat(map2.get("h")).isEqualTo("");
  }

  @Test
  void shouldBeAbleToDeserializeAnImmutableObject() throws Exception {
    var value = new ImmutableValue(1, "one", LocalDate.of(1996, 10, 6));
    var valueAsJson = objectMapper.writeValueAsString(value);
    var result = objectMapper.readValue(valueAsJson, ImmutableValue.class);

    assertThat(result.getId()).isEqualTo(1);
    assertThat(result.getName()).isEqualTo("one");
    assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1996, 10, 6));
  }

  @Test
  void shouldBeAbleToSerializeAndDeserializeARecord() throws Exception {
    var uuid = UUID.randomUUID();
    var value = new RecordValue(1, "one", LocalDate.of(1996, 10, 6), uuid);
    var valueAsJson = objectMapper.writeValueAsString(value);
    var result = objectMapper.readValue(valueAsJson, RecordValue.class);

    assertThat(result.id()).isEqualTo(1);
    assertThat(result.name()).isEqualTo("one");
    assertThat(result.birthDate()).isEqualTo(LocalDate.of(1996, 10, 6));
    assertThat(result.uuid()).isEqualTo(uuid);
  }

  @Test
  void shouldBeAbleToReadTheConstructorParameterNamesFromAJavaClass() {
    var parameters = ImmutableValue.class.getDeclaredConstructors()[0].getParameters();

    assertThat(parameters.length).isEqualTo(3);
    assertThat(parameters[0].getName()).isEqualTo("id");
    assertThat(parameters[1].getName()).isEqualTo("name");
    assertThat(parameters[2].getName()).isEqualTo("birthDate");
  }
}
