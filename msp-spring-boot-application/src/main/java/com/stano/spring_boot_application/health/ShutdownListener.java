package com.stano.spring_boot_application.health;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener {
  private final ReadinessState readinessState;

  public ShutdownListener(ReadinessState readinessState) {
    this.readinessState = readinessState;
  }

  @EventListener
  public void onShutdown(org.springframework.context.event.ContextClosedEvent event) {
    readinessState.setReady(false);
  }
}
