package com.stano.spring_boot_application.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

class DefaultPropertiesEnvironmentPostProcessorTest {

  @Test
  void defaultsAreAppliedWhenNoOverrideIsPresent() {
    StandardEnvironment environment = new StandardEnvironment();

    new DefaultPropertiesEnvironmentPostProcessor().postProcessEnvironment(environment, null);

    assertThat(environment.getProperty("spring.threads.virtual.enabled")).isEqualTo("true");
  }

  @Test
  void consumingApplicationPropertiesOverrideTheDefaults() {
    StandardEnvironment environment = new StandardEnvironment();
    environment
        .getPropertySources()
        .addFirst(
            new MapPropertySource(
                "consumingApplicationProperties",
                Map.of("management.endpoint.health.show-details", "always")));

    new DefaultPropertiesEnvironmentPostProcessor().postProcessEnvironment(environment, null);

    assertThat(environment.getProperty("management.endpoint.health.show-details"))
        .isEqualTo("always");
    assertThat(environment.getProperty("spring.threads.virtual.enabled")).isEqualTo("true");
  }
}
