package com.stano.domain_jpa;

import com.stano.domain_jpa.datasource.DataSourceFactory;
import com.stano.domain_jpa.jpa.hibernate.TraceIdStatementInspector;
import com.stano.domain_jpa.springdata.RoutingRepositoryFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = RoutingRepositoryFactoryBean.class)
@EnableTransactionManagement
public class DefaultJpaSpringConfig implements ImportAware {
  private Class<?>[] entityPackages = new Class<?>[0];
  private String importingClassName;

  @Override
  public void setImportMetadata(AnnotationMetadata metadata) {
    importingClassName = metadata.getClassName();
    AnnotationAttributes attrs = AnnotationAttributes.fromMap(
      metadata.getAnnotationAttributes(EnableJpa.class.getName()));
    if (attrs != null) {
      entityPackages = attrs.getClassArray("entityPackages");
    }
  }

  protected Class<?>[] getPackagesToScanForEntities() {
    return entityPackages;
  }

  @Bean
  public DataSource dataSource(ApplicationContext applicationContext) {
    DataSource dataSource = createDataSource(applicationContext);

    initializeDataSource(dataSource);
    dataSourceReady(dataSource);

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager(Environment environment, DataSource dataSource) {
    return createTransactionManager(environment, dataSource);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment environment,
                                                                     DataSource dataSource) {
    return createEntityManagerFactory(environment, dataSource);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter(Environment environment) {//DataSource dataSource
    return configureJpaVendorAdapter(createJpaVendorAdapter(environment));
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public EventListenerRegistry eventListenerRegistry(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory
             .unwrap(SessionFactoryImpl.class)
             .getServiceRegistry()
             .getService(EventListenerRegistry.class);
  }

  protected DataSource createDataSource(ApplicationContext applicationContext) {
    Environment environment = applicationContext.getEnvironment();
    DataSourceProperties dataSourceProperties = applicationContext.getBean(DataSourceProperties.class);

    return DataSourceFactory.createDataSource(environment,
                                              dataSourceProperties.getUrl(),
                                              dataSourceProperties.getUsername(),
                                              dataSourceProperties.getPassword());
  }

  protected void initializeDataSource(DataSource dataSource) {
  }

  protected void dataSourceReady(DataSource dataSource) {
  }

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
    hibernateJpaVendorAdapter.setShowSql(environment.getProperty("spring.jpa.properties.hibernate.show_sql",
                                                                 boolean.class,
                                                                 false));

    return hibernateJpaVendorAdapter;
  }

  protected JpaVendorAdapter configureJpaVendorAdapter(JpaVendorAdapter jpaVendorAdapter) {
    return jpaVendorAdapter;
  }

  protected Properties getJpaProperties(Environment environment) {
    Properties properties = new Properties();

    properties.put("hibernate.physical_naming_strategy",
                   environment.getProperty("spring.jpa.hibernate.naming.physical-strategy",
                                           PhysicalNamingStrategySnakeCaseImpl.class.getName()));
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
                                           TraceIdStatementInspector.class.getName()));
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

  private String[] getPackagesToScan() {
    List<String> packages = new ArrayList<>();
    packages.add("com.stano.domain_jpa.jpa.converters");
    Class<?>[] entityClasses = getPackagesToScanForEntities();
    if (entityClasses.length > 0) {
      Arrays.stream(entityClasses)
            .map(clazz -> clazz.getPackage().getName())
            .forEach(packages::add);
    } else if (importingClassName != null) {
      int dot = importingClassName.lastIndexOf('.');
      if (dot > 0) {
        packages.add(importingClassName.substring(0, dot));
      }
    } else {
      packages.add(getClass().getPackageName());
    }
    return packages.toArray(new String[0]);
  }
}
