package com.stano.spring_boot_application.config;

import java.io.IOException;
import java.util.List;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Loads platform-wide defaults from {@code msp-default.yaml} and adds them as the lowest-priority
 * property source. A regular {@code classpath:/application.yaml} can't be used for this because
 * Spring Boot resolves that location to a single resource, so a consuming application's own {@code
 * application.yaml} would silently replace these defaults instead of layering on top of them.
 */
public class DefaultPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

  private static final String RESOURCE_LOCATION = "msp-default.yaml";

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    Resource resource = new ClassPathResource(RESOURCE_LOCATION);
    if (!resource.exists()) {
      return;
    }

    List<PropertySource<?>> propertySources;
    try {
      propertySources = new YamlPropertySourceLoader().load(RESOURCE_LOCATION, resource);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load " + RESOURCE_LOCATION, e);
    }

    for (PropertySource<?> propertySource : propertySources) {
      environment.getPropertySources().addLast(propertySource);
    }
  }
}
