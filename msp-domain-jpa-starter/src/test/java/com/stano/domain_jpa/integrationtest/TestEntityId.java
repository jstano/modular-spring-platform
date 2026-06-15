package com.stano.domain_jpa.integrationtest;

import com.stano.domain_jpa.id.EntityId;
import java.util.UUID;

public class TestEntityId extends EntityId {
  public TestEntityId(UUID value) {
    super(value);
  }
}
