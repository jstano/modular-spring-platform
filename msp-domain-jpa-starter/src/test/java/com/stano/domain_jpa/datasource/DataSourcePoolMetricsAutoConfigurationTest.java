package com.stano.domain_jpa.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.stano.schema.installer.schemacontext.SchemaContext;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jdbc.autoconfigure.metrics.DataSourcePoolMetricsAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.MetricsAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class DataSourcePoolMetricsAutoConfigurationTest {
  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  JpaDataSourceAutoConfiguration.class,
                  DataSourcePoolMetricsAutoConfiguration.class,
                  MetricsAutoConfiguration.class,
                  CompositeMeterRegistryAutoConfiguration.class,
                  SimpleMetricsExportAutoConfiguration.class));

  @Test
  void hikariConnectionPoolMetricsAreRegisteredForTheDataSourceBean() throws Exception {
    var schemaContext = mock(SchemaContext.class);
    when(schemaContext.schemaIsInstalled(any())).thenReturn(true);
    when(schemaContext.getMigrationScriptLocator(any())).thenReturn("classpath:db/migration/empty");

    contextRunner
        .withBean(SchemaContext.class, () -> schemaContext)
        .withPropertyValues(
            "spring.datasource.url=jdbc:h2:mem:datasourcepoolmetricstest;DB_CLOSE_DELAY=-1",
            "spring.datasource.username=sa",
            "spring.datasource.password=")
        .run(
            context -> {
              MeterRegistry registry = context.getBean(MeterRegistry.class);
              assertThat(registry.find("hikaricp.connections.max").meter()).isNotNull();
              assertThat(registry.find("hikaricp.connections.active").meter()).isNotNull();
            });
  }
}
