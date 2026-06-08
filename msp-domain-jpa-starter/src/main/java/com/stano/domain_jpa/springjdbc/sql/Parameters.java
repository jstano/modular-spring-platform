package com.stano.domain_jpa.springjdbc.sql;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Parameters {
  private final Sql sql;
  private final Map<String, Object> parameterMap = new HashMap<>();

  public Map<String, Object> getParameterMap() {
    return Collections.unmodifiableMap(new HashMap<>(parameterMap));
  }

  public void setParameter(String name, boolean value) {
    Sql.BooleanMode booleanMode = sql.getBooleanMode();

    if (booleanMode == Sql.BooleanMode.YN) {
      parameterMap.put(name, value ? "Y" : "N");
    }
    else if (booleanMode == Sql.BooleanMode.YES_NO) {
      parameterMap.put(name, value ? "Yes" : "No");
    }
    else {
      parameterMap.put(name, value);
    }
  }

  public void setParameter(String name, Object value) {
    if (value instanceof Boolean) {
      setParameter(name, ((Boolean)value).booleanValue());
    }
    else {
      parameterMap.put(name, value);
    }
  }

  public <T> void setParameter(String name, Collection<T> value) {
    parameterMap.put(name, value);
  }

  Parameters(Sql sql) {
    this.sql = sql;
  }
}
