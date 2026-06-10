package com.stano.spring_boot_application.schema;

import org.springframework.context.ApplicationEvent;

public class SchemaMigrationRequestedEvent extends ApplicationEvent {
  private boolean handled = false;

  public SchemaMigrationRequestedEvent(Object source) {
    super(source);
  }

  public void markHandled() {
    this.handled = true;
  }

  public boolean isHandled() {
    return this.handled;
  }
}
