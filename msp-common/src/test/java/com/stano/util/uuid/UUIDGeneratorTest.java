package com.stano.util.uuid;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UUIDGeneratorTest {

  @Test
  void randomUUIDShouldReturnAVersion4UUID() {
    UUID uuid = UUIDGenerator.randomUUID();
    assertThat(uuid).isNotNull();
    assertThat(uuid.version()).isEqualTo(4);
  }

  @Test
  void timeOrderedUUIDShouldReturnAVersion7UUID() {
    UUID uuid = UUIDGenerator.timeOrderedUUID();
    assertThat(uuid).isNotNull();
    assertThat(uuid.version()).isEqualTo(7);
  }

  @Test
  void successiveTimeOrderedUUIDsShouldBeMonotonicallyIncreasing() {
    UUID first = UUIDGenerator.timeOrderedUUID();
    UUID second = UUIDGenerator.timeOrderedUUID();
    assertThat(first.compareTo(second)).isLessThanOrEqualTo(0);
  }
}
