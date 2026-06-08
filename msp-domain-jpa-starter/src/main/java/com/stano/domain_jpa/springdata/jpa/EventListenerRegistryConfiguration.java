package com.stano.domain_jpa.springdata.jpa;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventListenerRegistryConfiguration {
  private final EntityManagerFactory entityManagerFactory;

  EventListenerRegistryConfiguration(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Bean
  public EventListenerRegistry eventListenerRegistry() {
    return entityManagerFactory
        .unwrap(SessionFactoryImpl.class)
        .getServiceRegistry()
        .getService(EventListenerRegistry.class);
  }
}
