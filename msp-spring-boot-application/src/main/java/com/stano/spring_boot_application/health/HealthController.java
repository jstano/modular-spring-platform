package com.stano.spring_boot_application.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
  private final ReadinessState readinessState;

  public HealthController(ReadinessState readinessState) {
    this.readinessState = readinessState;
  }

  @GetMapping
  public ResponseEntity<Void> getHealth() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/liveness")
  public ResponseEntity<Void> getLiveness() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/readiness")
  public ResponseEntity<Void> readiness() {
    if (readinessState.isReady()) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(503).build();
    }
  }
}
