package com.stano.domain_jpa.metrics;

public record ConnectionPoolMetrics(
  int activeConnections,
  int poolSize,
  int maxPoolSize,
  long keepaliveTime,
  long connectionTimeout,
  long maxLifeTime,
  int threadsAwaitingConnection) {
}
