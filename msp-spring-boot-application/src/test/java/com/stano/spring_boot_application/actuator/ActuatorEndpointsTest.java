package com.stano.spring_boot_application.actuator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class ActuatorEndpointsTest {
  @LocalServerPort private int port;

  private RestTestClient restTestClient;

  @BeforeEach
  void setUp() {
    restTestClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
  }

  @Test
  void healthEndpointReturnsUpStatusWithoutComponentDetails() {
    restTestClient
        .get()
        .uri("/health")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.OK)
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("UP")
        .jsonPath("$.components")
        .doesNotExist();
  }

  @Test
  void healthLivenessProbeReturnsUpStatus() {
    restTestClient
        .get()
        .uri("/health/liveness")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.OK)
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("UP");
  }

  @Test
  void healthReadinessProbeReturnsUpStatus() {
    restTestClient
        .get()
        .uri("/health/readiness")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.OK)
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("UP");
  }

  @Test
  void metricsEndpointReturnsPrometheusScrapeWithJvmMetrics() {
    String body =
        restTestClient
            .get()
            .uri("/metrics")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

    assertThat(body).contains("jvm_memory_used_bytes");
    assertThat(body).contains("process_uptime_seconds");
  }

  @Test
  void infoEndpointIsExposed() {
    restTestClient.get().uri("/info").exchange().expectStatus().isEqualTo(HttpStatus.OK);
  }

  @Test
  void endpointsNotInExposureListAreNotReachable() {
    for (String path :
        new String[] {"/env", "/beans", "/threaddump", "/heapdump", "/mappings", "/loggers"}) {
      restTestClient.get().uri(path).exchange().expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }
  }
}
