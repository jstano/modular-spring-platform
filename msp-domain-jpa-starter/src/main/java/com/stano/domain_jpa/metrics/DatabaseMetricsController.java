package com.stano.domain_jpa.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnWebApplication
@RequestMapping("/metrics")
public class DatabaseMetricsController {
  private final DatabaseMetricsService databaseMetricsService;

  public DatabaseMetricsController(DatabaseMetricsService databaseMetricsService) {
    this.databaseMetricsService = databaseMetricsService;
  }

  @GetMapping("/database")
  public ConnectionPoolMetrics getDatabaseMetrics() {
    return databaseMetricsService.getConnectionPoolMetrics();
  }
}
