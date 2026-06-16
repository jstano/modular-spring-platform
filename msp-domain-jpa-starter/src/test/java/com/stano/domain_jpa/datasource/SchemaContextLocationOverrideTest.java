package com.stano.domain_jpa.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.stano.schema.installer.schemacontext.DefaultSchemaContext;
import com.stano.schema.installer.schemacontext.SchemaContext;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class SchemaContextLocationOverrideTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JpaDataSourceAutoConfiguration.class))
          .withBean(DataSource.class, () -> mock(DataSource.class));

  @Test
  void overridingLocationPicksUpCustomResource() {
    contextRunner
        .withPropertyValues("msp.jpa.schema.location=custom-schema.xml")
        .run(
            context -> {
              assertThat(context).hasSingleBean(SchemaContext.class);
              SchemaContext schemaContext = context.getBean(SchemaContext.class);
              assertThat(schemaContext).isInstanceOf(DefaultSchemaContext.class);
              assertThat(schemaContext.getSchemaUrl().toString()).contains("custom-schema.xml");
            });
  }

  @Test
  void overriddenLocationThatDoesNotExistMeansNoSchemaContextBean() {
    contextRunner
        .withPropertyValues("msp.jpa.schema.location=does-not-exist.xml")
        .run(context -> assertThat(context).doesNotHaveBean(SchemaContext.class));
  }
}
