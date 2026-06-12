package com.stano.domain_jpa.routable_datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoutableDataSourcesTest {
  private RoutableDataSources<Integer, String> dataSources;

  @BeforeEach
  void setup() {
    dataSources =
        new RoutableDataSources<>(
            Map.of(
                123, "123",
                456, "456",
                789, "789"));
  }

  @Test
  void testBasicOperations() {
    assertThat(dataSources.size()).isEqualTo(3);
    assertThat(dataSources.isEmpty()).isFalse();

    assertThat(dataSources.containsKey(123)).isTrue();
    assertThat(dataSources.containsKey(456)).isTrue();
    assertThat(dataSources.containsKey(789)).isTrue();
    assertThat(dataSources.containsKey(987)).isFalse();

    assertThat(dataSources.containsValue("123")).isTrue();
    assertThat(dataSources.containsValue("456")).isTrue();
    assertThat(dataSources.containsValue("789")).isTrue();
    assertThat(dataSources.containsValue("987")).isFalse();

    assertThat(dataSources.get(123)).isEqualTo("123");
    assertThat(dataSources.get(456)).isEqualTo("456");
    assertThat(dataSources.get(789)).isEqualTo("789");
    assertThat(dataSources.get(987)).isNull();

    assertThat(dataSources.keySet().size()).isEqualTo(3);
  }

  @Test
  void keySetShouldReturnTheCorrectSizeAndValues() {
    var keySet = dataSources.keySet();

    assertThat(keySet.size()).isEqualTo(3);
    assertThat(keySet).contains(123, 456, 789).doesNotContain(987);
  }

  @Test
  void valuesShouldReturnTheCorrectSizeAndValues() {
    var values = dataSources.values();

    assertThat(values.size()).isEqualTo(3);
    assertThat(values).contains("123", "456", "789").doesNotContain("987");
  }

  @Test
  void entrySetShouldReturnTheCorrectSizeAndValues() {
    var entrySet = dataSources.entrySet();

    assertThat(entrySet.size()).isEqualTo(3);
    assertThat(entrySet)
        .anySatisfy(
            e -> {
              assertThat(e.getKey()).isEqualTo(123);
              assertThat(e.getValue()).isEqualTo("123");
            })
        .anySatisfy(
            e -> {
              assertThat(e.getKey()).isEqualTo(456);
              assertThat(e.getValue()).isEqualTo("456");
            })
        .anySatisfy(
            e -> {
              assertThat(e.getKey()).isEqualTo(789);
              assertThat(e.getValue()).isEqualTo("789");
            });
    assertThat(entrySet.stream().noneMatch(e -> e.getKey() == 987)).isTrue();
  }

  @Test
  void shouldBeAbleToGetWhatIsPut() {
    dataSources.put(765, "765");

    assertThat(dataSources.get(765)).isEqualTo("765");
  }

  @Test
  void removeShouldThrowUnsupportedOperationException() {
    assertThatThrownBy(() -> dataSources.remove(765))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void putAllShouldThrowUnsupportedOperationException() {
    assertThatThrownBy(() -> dataSources.putAll(Map.of()))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void clearShouldThrowUnsupportedOperationException() {
    assertThatThrownBy(() -> dataSources.clear()).isInstanceOf(UnsupportedOperationException.class);
  }
}
