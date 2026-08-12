package com.stano.domain_jpa.jpa.hibernate;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes Hibernate's {@link EventListenerRegistry} as a Spring bean by unwrapping the injected
 * {@link EntityManagerFactory}, so other beans can obtain the registry directly (for example, to
 * register listeners outside the flow handled by {@link EventListenerAutoConfiguration}).
 */
@Configuration
class EventListenerRegistryConfiguration {
  private final EntityManagerFactory entityManagerFactory;

  /**
   * Creates this configuration with the entity manager factory to unwrap for Hibernate internals.
   *
   * @param entityManagerFactory the JPA entity manager factory to unwrap
   */
  EventListenerRegistryConfiguration(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * Resolves Hibernate's {@link EventListenerRegistry} from the underlying session factory.
   *
   * @return the Hibernate event listener registry
   */
  @Bean
  public EventListenerRegistry eventListenerRegistry() {
    return entityManagerFactory
        .unwrap(SessionFactoryImpl.class)
        .getServiceRegistry()
        .getService(EventListenerRegistry.class);
  }
}
