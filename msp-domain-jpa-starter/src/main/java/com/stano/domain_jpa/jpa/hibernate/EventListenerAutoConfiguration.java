package com.stano.domain_jpa.jpa.hibernate;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreLoadEventListener;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Registers any Spring-managed Hibernate event listener beans (e.g. {@link PreInsertEventListener},
 * {@link PostUpdateEventListener}) with Hibernate's {@link EventListenerRegistry}, so application
 * modules can contribute listeners simply by declaring them as Spring beans. Runs after {@link
 * HibernateJpaAutoConfiguration} and only when an {@link EntityManagerFactory} bean is present.
 */
@AutoConfiguration(after = HibernateJpaAutoConfiguration.class)
@ConditionalOnBean(EntityManagerFactory.class)
class EventListenerAutoConfiguration {
  /**
   * Appends every available Spring-managed Hibernate event listener bean to the corresponding
   * {@link EventListenerRegistry} group, unwrapping the given {@link EntityManagerFactory} to reach
   * Hibernate's underlying service registry.
   *
   * @param entityManagerFactory the JPA entity manager factory to unwrap for Hibernate internals
   * @param preInsert available {@link PreInsertEventListener} beans
   * @param postInsert available {@link PostInsertEventListener} beans
   * @param preUpdate available {@link PreUpdateEventListener} beans
   * @param postUpdate available {@link PostUpdateEventListener} beans
   * @param preDelete available {@link PreDeleteEventListener} beans
   * @param postDelete available {@link PostDeleteEventListener} beans
   * @param preLoad available {@link PreLoadEventListener} beans
   * @param postLoad available {@link PostLoadEventListener} beans
   * @return the Hibernate event listener registry, with all discovered listeners registered
   */
  @Bean
  public EventListenerRegistry eventListenerRegistry(
      EntityManagerFactory entityManagerFactory,
      ObjectProvider<PreInsertEventListener> preInsert,
      ObjectProvider<PostInsertEventListener> postInsert,
      ObjectProvider<PreUpdateEventListener> preUpdate,
      ObjectProvider<PostUpdateEventListener> postUpdate,
      ObjectProvider<PreDeleteEventListener> preDelete,
      ObjectProvider<PostDeleteEventListener> postDelete,
      ObjectProvider<PreLoadEventListener> preLoad,
      ObjectProvider<PostLoadEventListener> postLoad) {
    EventListenerRegistry registry =
        entityManagerFactory
            .unwrap(SessionFactoryImpl.class)
            .getServiceRegistry()
            .getService(EventListenerRegistry.class);
    preInsert.forEach(l -> registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(l));
    postInsert.forEach(
        l -> registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(l));
    preUpdate.forEach(l -> registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(l));
    postUpdate.forEach(
        l -> registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(l));
    preDelete.forEach(l -> registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(l));
    postDelete.forEach(
        l -> registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(l));
    preLoad.forEach(l -> registry.getEventListenerGroup(EventType.PRE_LOAD).appendListener(l));
    postLoad.forEach(l -> registry.getEventListenerGroup(EventType.POST_LOAD).appendListener(l));
    return registry;
  }
}
