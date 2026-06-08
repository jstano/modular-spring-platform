package com.stano.spring_boot_application.health;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {
  private final ReadinessState readinessState;

  public StartupListener(ReadinessState readinessState) {
    this.readinessState = readinessState;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onReady() {
    readinessState.setReady(true);
  }
}
