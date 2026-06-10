package com.stano.domain_jpa.jpa.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;

public class TraceIdStatementInspector implements StatementInspector {
  private static final String TRACE_ID = "traceId";

  @Override
  public String inspect(String sql) {
    var traceId = MDC.get(TRACE_ID);

    if (traceId != null) {
      return " /* traceId: " + traceId + " */ " + sql;
    }

    return sql;
  }
}
