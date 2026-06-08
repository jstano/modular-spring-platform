package com.stano.domain_jpa.springdata.jpa;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;

public class RequestLoggingStatementInspector implements StatementInspector {
  private static final String REQUEST_HEADER_ID = "request_id";

  @Override
  public String inspect(String sql) {
    var requestId = MDC.get(REQUEST_HEADER_ID);

    if (requestId != null) {
      return " /* request_id: " + requestId + " */ " + sql;
    }

    return sql;
  }
}
