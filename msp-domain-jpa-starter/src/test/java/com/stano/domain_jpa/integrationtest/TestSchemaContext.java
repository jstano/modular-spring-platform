package com.stano.domain_jpa.integrationtest;

import com.stano.schema.installer.schemacontext.DefaultSchemaContext;

class TestSchemaContext extends DefaultSchemaContext {
  TestSchemaContext() {
    super(TestSchemaContext.class.getClassLoader().getResource("schema.xml"), "db/migration/test");
  }
}
