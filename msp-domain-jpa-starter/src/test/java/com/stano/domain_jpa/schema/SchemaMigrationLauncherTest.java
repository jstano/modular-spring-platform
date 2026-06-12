package com.stano.domain_jpa.schema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.stano.schema.installer.schemacontext.SchemaContext;
import org.junit.jupiter.api.Test;

class SchemaMigrationLauncherTest {

  @Test
  void returnsFalseWhenNoMigrateFlag() {
    String[] args = {"--some-other-arg"};
    SchemaContext schemaContext = mock(SchemaContext.class);

    boolean result = SchemaMigrationLauncher.handleArgs(args, schemaContext);

    assertThat(result).isFalse();
  }

  @Test
  void returnsFalseWhenArgsNull() {
    String[] args = null;
    SchemaContext schemaContext = mock(SchemaContext.class);

    boolean result =
        SchemaMigrationLauncher.handleArgs(args == null ? new String[0] : args, schemaContext);

    assertThat(result).isFalse();
  }

  @Test
  void throwsWhenMigrateFlagButMissingUrl() {
    String[] args = {"--migrate"};
    SchemaContext schemaContext = mock(SchemaContext.class);

    assertThatThrownBy(() -> SchemaMigrationLauncher.handleArgs(args, schemaContext))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("required datasource properties not found");
  }

  @Test
  void throwsWhenMigrateFlagButMissingUsername() {
    String[] args = {"--migrate"};
    SchemaContext schemaContext = mock(SchemaContext.class);

    System.setProperty("SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost/test");

    try {
      assertThatThrownBy(() -> SchemaMigrationLauncher.handleArgs(args, schemaContext))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("required datasource properties not found");
    } finally {
      System.clearProperty("SPRING_DATASOURCE_URL");
    }
  }
}
