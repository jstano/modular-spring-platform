package com.stano.domain_jpa.jpa.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;

/**
 * Hibernate {@link StatementInspector} that prepends the current request's trace id, read from
 * SLF4J's {@link MDC}, as a SQL comment on every executed statement. Configured as the platform's
 * default {@code hibernate.session_factory.statement_inspector} by {@link
 * com.stano.domain_jpa.DefaultJpaSpringConfig}, this makes it possible to correlate slow-query or
 * database-log SQL with the application trace that issued it.
 */
public class TraceIdStatementInspector implements StatementInspector {
  private static final String TRACE_ID = "traceId";

  /**
   * Prepends a SQL comment containing the current trace id to {@code sql} when one is present in
   * the MDC, otherwise returns {@code sql} unchanged.
   *
   * @param sql the SQL statement about to be executed
   * @return the SQL statement, optionally prefixed with a trace id comment
   */
  @Override
  public String inspect(String sql) {
    var traceId = MDC.get(TRACE_ID);

    if (traceId != null) {
      return " /* traceId: " + traceId + " */ " + sql;
    }

    return sql;
  }
}
