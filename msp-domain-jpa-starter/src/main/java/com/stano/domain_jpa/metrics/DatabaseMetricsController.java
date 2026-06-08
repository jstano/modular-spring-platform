package com.stano.domain_jpa.metrics;

import tools.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class DatabaseMetricsController {
  private final ObjectMapper objectMapper;
  private final ConfigurableEnvironment configurableEnvironment;
  private final MeterRegistry meterRegistry;
  private final DatabaseMetricsService databaseMetricsService;
  private final PrometheusMeterRegistry registry;

  public DatabaseMetricsController(ObjectMapper objectMapper,
                                   ConfigurableEnvironment configurableEnvironment,
                                   MeterRegistry meterRegistry,
                                   DatabaseMetricsService databaseMetricsService,
                                   PrometheusMeterRegistry registry) {
    this.objectMapper = objectMapper;
    this.configurableEnvironment = configurableEnvironment;
    this.meterRegistry = meterRegistry;
    this.databaseMetricsService = databaseMetricsService;
    this.registry = registry;
  }

  @GetMapping("/database")
  public ConnectionPoolMetrics getDatabaseMetrics() {
    return databaseMetricsService.getConnectionPoolMetrics();
  }
}
