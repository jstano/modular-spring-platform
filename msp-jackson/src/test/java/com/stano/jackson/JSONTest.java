package com.stano.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

class JSONTest {

  @Test
  void convertingAnObjectToAJsonStringAndThenBackToAnObjectShouldWork() {
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

    var jsonStr = JSON.toString(testDTO);
    var resultDTO = JSON.parse(jsonStr, TestDTO.class);

    assertThat(resultDTO.getId()).isEqualTo(testDTO.getId());
    assertThat(resultDTO.getName()).isEqualTo(testDTO.getName());
    assertThat(resultDTO.getLocalDate()).isEqualTo(testDTO.getLocalDate());
    assertThat(resultDTO.getLocalDateTime()).isEqualTo(testDTO.getLocalDateTime());
    assertThat(resultDTO.getLocalTime()).isEqualTo(testDTO.getLocalTime());
    assertThat(resultDTO.getOptionalInt().getAsInt()).isEqualTo(234);
  }

  @Test
  void jsonParseShouldReturnNullIfTheJsonTextParameterIsNullOrBlank() {
    assertThat(JSON.parse(null, Object.class)).isNull();
    assertThat(JSON.parse("", Object.class)).isNull();
    assertThat(JSON.parse(" ", Object.class)).isNull();
  }

  @Test
  void jsonParseShouldReturnNullIfTheJsonTextEqualsTheStringNull() {
    assertThat(JSON.parse("null", Object.class)).isNull();
  }

  @Test
  void jsonToStringNullShouldReturnNull() {
    assertThat(JSON.toString(null)).isEqualTo("null");
  }
}
