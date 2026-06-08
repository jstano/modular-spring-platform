package com.stano.spring_boot_application.health;

import org.springframework.stereotype.Component;

@Component
public class ReadinessState {
  private volatile boolean ready = false;

  public boolean isReady() {
    return ready;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }
}
