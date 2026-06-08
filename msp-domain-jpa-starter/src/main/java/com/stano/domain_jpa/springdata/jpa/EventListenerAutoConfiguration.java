package com.stano.domain_jpa.springdata.jpa;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventListenerAutoConfiguration {
  private final EventListenerRegistry eventListenerRegistry;

  public EventListenerAutoConfiguration(EventListenerRegistry eventListenerRegistry) {
    this.eventListenerRegistry = eventListenerRegistry;
  }

  @Autowired(required = false)
  public void register(PreInsertEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_INSERT).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PostInsertEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.POST_INSERT).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PreUpdateEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_UPDATE).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PostUpdateEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.POST_UPDATE).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PreDeleteEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_DELETE).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PostDeleteEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.POST_DELETE).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PreLoadEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.PRE_LOAD).appendListeners(listeners);
  }

  @Autowired(required = false)
  public void register(PostLoadEventListener[] listeners) {
    eventListenerRegistry.getEventListenerGroup(EventType.POST_LOAD).appendListeners(listeners);
  }
}
