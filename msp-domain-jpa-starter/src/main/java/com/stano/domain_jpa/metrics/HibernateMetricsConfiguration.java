package com.stano.domain_jpa.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.stat.HibernateMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class HibernateMetricsConfiguration implements SmartInitializingSingleton {
  private static final Logger log = LoggerFactory.getLogger(HibernateMetricsConfiguration.class);

  private static final String ENTITY_MANAGER_FACTORY_SUFFIX = "entityManagerFactory";

  private final ConfigurableListableBeanFactory beanFactory;
  private final MeterRegistry meterRegistry;

  public HibernateMetricsConfiguration(
      ConfigurableListableBeanFactory beanFactory, MeterRegistry meterRegistry) {
    this.beanFactory = beanFactory;
    this.meterRegistry = meterRegistry;
  }

  @Override
  public void afterSingletonsInstantiated() {
    var entityManagerFactories =
        SimpleAutowireCandidateResolver.resolveAutowireCandidates(
            beanFactory, EntityManagerFactory.class);

    bindEntityManagerFactoriesToRegistry(entityManagerFactories, this.meterRegistry);
  }

  private void bindEntityManagerFactoriesToRegistry(
      Map<String, EntityManagerFactory> entityManagerFactories, MeterRegistry registry) {
    entityManagerFactories.forEach(
        (name, factory) -> bindEntityManagerFactoryToRegistry(name, factory, registry));
  }

  private void bindEntityManagerFactoryToRegistry(
      String beanName, EntityManagerFactory entityManagerFactory, MeterRegistry registry) {
    try {
      String entityManagerFactoryName = getEntityManagerFactoryName(beanName);
      new HibernateMetrics(
              entityManagerFactory.unwrap(SessionFactory.class),
              entityManagerFactoryName,
              Collections.emptyList())
          .bindTo(registry);
    } catch (PersistenceException e) {
      log.warn(
          "Failed to unwrap EntityManagerFactory '{}' to SessionFactory; Hibernate metrics will not"
              + " be registered",
          beanName);
    }
  }

  private String getEntityManagerFactoryName(String beanName) {
    if (beanName.length() > ENTITY_MANAGER_FACTORY_SUFFIX.length()
        && StringUtils.endsWithIgnoreCase(beanName, ENTITY_MANAGER_FACTORY_SUFFIX)) {
      return beanName.substring(0, beanName.length() - ENTITY_MANAGER_FACTORY_SUFFIX.length());
    }
    return beanName;
  }
}
