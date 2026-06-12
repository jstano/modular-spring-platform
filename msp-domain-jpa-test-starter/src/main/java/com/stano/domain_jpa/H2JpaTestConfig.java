package com.stano.domain_jpa;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@EnableJpa
@SpringBootConfiguration
public class H2JpaTestConfig extends DefaultJpaSpringConfig {
  @Override
  protected DataSource createDataSource(ApplicationContext ctx) {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
  }

  @Override
  protected JpaVendorAdapter configureJpaVendorAdapter(JpaVendorAdapter jpaVendorAdapter) {
    HibernateJpaVendorAdapter adapter = (HibernateJpaVendorAdapter) jpaVendorAdapter;
    adapter.setGenerateDdl(true);
    adapter.setDatabase(Database.H2);
    return adapter;
  }

  @Override
  protected Properties getJpaProperties(Environment environment) {
    Properties jpaProperties = super.getJpaProperties(environment);
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    return jpaProperties;
  }
}
