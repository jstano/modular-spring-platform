package com.stano.domain_jpa.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.stano.schema.installer.schemacontext.SchemaContext;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class JpaDataSourceAutoConfigurationTest {
  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JpaDataSourceAutoConfiguration.class));

  @Test
  void dataSourcePropertiesBindsUrlUsernameAndPassword() {
    contextRunner
        .withBean(DataSource.class, () -> mock(DataSource.class))
        .withPropertyValues(
            "spring.datasource.url=jdbc:h2:mem:testdb",
            "spring.datasource.username=sa",
            "spring.datasource.password=secret")
        .run(
            context -> {
              assertThat(context).hasSingleBean(DataSourceProperties.class);
              var props = context.getBean(DataSourceProperties.class);
              assertThat(props.getUrl()).isEqualTo("jdbc:h2:mem:testdb");
              assertThat(props.getUsername()).isEqualTo("sa");
              assertThat(props.getPassword()).isEqualTo("secret");
            });
  }

  @Test
  void dataSourceBeanIsCreatedUsingBoundProperties() throws SQLException {
    var schemaContext = mock(SchemaContext.class);
    when(schemaContext.schemaIsInstalled(org.mockito.ArgumentMatchers.any())).thenReturn(true);
    when(schemaContext.getMigrationScriptLocator(org.mockito.ArgumentMatchers.any()))
        .thenReturn("classpath:db/migration/empty");

    contextRunner
        .withBean(SchemaContext.class, () -> schemaContext)
        .withPropertyValues(
            "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            "spring.datasource.username=sa",
            "spring.datasource.password=")
        .run(
            context -> {
              assertThat(context).hasSingleBean(DataSource.class);
              var dataSource = context.getBean(DataSource.class);
              assertThat(dataSource).isInstanceOf(HikariDataSource.class);
              assertThat(((HikariDataSource) dataSource).getJdbcUrl())
                  .isEqualTo("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            });
  }

  @Test
  void dataSourcePropertiesIsAlwaysRegisteredAndBound() {
    contextRunner
        .withBean(DataSource.class, () -> mock(DataSource.class))
        .withPropertyValues("spring.datasource.url=jdbc:h2:mem:custom")
        .run(
            context -> {
              assertThat(context).hasSingleBean(DataSourceProperties.class);
              assertThat(context.getBean(DataSourceProperties.class).getUrl())
                  .isEqualTo("jdbc:h2:mem:custom");
            });
  }

  @Test
  void dataSourceBeanIsNotCreatedWhenAlreadyPresent() {
    var existingDataSource = mock(DataSource.class);

    contextRunner
        .withBean(DataSource.class, () -> existingDataSource)
        .run(
            context -> {
              assertThat(context).hasSingleBean(DataSource.class);
              assertThat(context.getBean(DataSource.class)).isSameAs(existingDataSource);
              assertThat(context.getBean(DataSource.class)).isNotInstanceOf(HikariDataSource.class);
            });
  }
}
