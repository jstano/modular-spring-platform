package com.stano.domain_jpa.springdata;

import com.stano.domain_jpa.hibernate.CustomPostgreSQLDialect;
import com.stano.domain_jpa.hibernate.LowerCaseNamingStrategy;
import com.stano.domain_jpa.springdata.jpa.RequestLoggingStatementInspector;
import com.stano.domain_jpa.springjdbc.sql.Sql;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@EnableTransactionManagement
public abstract class DefaultJpaPersistenceAdapterSpringConfig extends
                                                               AbstractPersistenceAdapterSpringConfig {
  protected abstract Class<?>[] getPackagesToScanForEntities();

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment environment,
                                                                     DataSource dataSource) {
    return createEntityManagerFactory(environment, dataSource);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter(Environment environment) {//DataSource dataSource
    return createJpaVendorAdapter(environment);
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public Sql sql(JdbcTemplate jdbcTemplate) {
    return new Sql(jdbcTemplate);
  }

  @Bean
  public EventListenerRegistry eventListenerRegistry(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory
             .unwrap(SessionFactoryImpl.class)
             .getServiceRegistry()
             .getService(EventListenerRegistry.class);
  }

  @Override
  protected PlatformTransactionManager createTransactionManager(Environment environment,
                                                                DataSource dataSource) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(entityManagerFactory(environment,
                                                                       dataSource).getObject());
    jpaTransactionManager.setDataSource(dataSource);

    return jpaTransactionManager;
  }

  protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory(Environment environment,
                                                                              DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setJpaProperties(getJpaProperties(environment));
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter(environment));
    entityManagerFactoryBean.setPackagesToScan(getPackagesToScan());
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.afterPropertiesSet();

    return entityManagerFactoryBean;
  }

  protected JpaVendorAdapter createJpaVendorAdapter(Environment environment) {
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
    hibernateJpaVendorAdapter.setGenerateDdl(false);
    hibernateJpaVendorAdapter.setShowSql(showSql(environment));

    return hibernateJpaVendorAdapter;
  }

  protected Properties getJpaProperties(Environment environment) {
    Properties properties = new Properties();
    properties.put("hibernate.dialect",
                   environment.getProperty("spring.jpa.properties.hibernate.dialect",
                                           CustomPostgreSQLDialect.class.getName()));

    String namingStrategy = environment.getProperty("spring.jpa.hibernate.naming.physical-strategy");
    if (namingStrategy != null) {
      properties.put("hibernate.physical_naming_strategy", namingStrategy);
    }
    else if (useUnderscoreNamingStrategy()) {
      properties.put("hibernate.physical_naming_strategy",
                     PhysicalNamingStrategySnakeCaseImpl.class.getName());
    }
    else if (useLowerCaseNamingStrategy()) {
      properties.put("hibernate.physical_naming_strategy", LowerCaseNamingStrategy.class.getName());
    }

    properties.put("hibernate.jdbc.batch_size",
                   environment.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size", Integer.class, 200));
    properties.put("hibernate.order_inserts",
                   environment.getProperty("spring.jpa.properties.hibernate.order_inserts", boolean.class, true));
    properties.put("hibernate.jdbc.lob.non_contextual_creation",
                   environment.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", boolean.class, true));
    properties.put("hibernate.jdbc.time_zone",
                   environment.getProperty("spring.jpa.properties.hibernate.jdbc.time_zone", "UTC"));
    properties.put("hibernate.session_factory.statement_inspector",
                   environment.getProperty("spring.jpa.properties.hibernate.session_factory.statement_inspector",
                                           RequestLoggingStatementInspector.class.getName()));
    properties.put("hibernate.show_sql",
                   environment.getProperty("spring.jpa.properties.hibernate.show_sql", boolean.class, false));
    properties.put("hibernate.format_sql",
                   environment.getProperty("spring.jpa.properties.hibernate.format_sql", boolean.class, false));
    properties.put("hibernate.hbm2ddl.auto",
                   environment.getProperty("spring.jpa.hibernate.ddl-auto", "none"));
    properties.put("hibernate.generate_statistics",
                   environment.getProperty("spring.jpa.properties.hibernate.generate_statistics", boolean.class, false));

    return properties;
  }

  protected boolean useUnderscoreNamingStrategy() {
    return false;
  }

  protected boolean useLowerCaseNamingStrategy() {
    return false;
  }

  private String[] getPackagesToScan() {
    List<String> packages = new ArrayList<>();
    packages.add("com.stano.domain_jpa");
    Arrays.stream(getPackagesToScanForEntities())
          .map(clazz -> clazz.getPackage().getName())
          .forEach(packages::add);
    return packages.toArray(new String[0]);
  }

  private boolean showSql(Environment environment) {
    return environment.getProperty("stano.jpa.show-sql", Boolean.class, false);
  }
}
