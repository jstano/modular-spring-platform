package com.stano.domain_jpa.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(
    classes = MetricsEndpointTestConfig.class,
    webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:metricsendpointtest;DB_CLOSE_DELAY=-1",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.properties.hibernate.generate_statistics=true",
      "msp.schema.skip-migration-check=true",
      "spring.test.database.replace=none",
      "management.endpoints.web.exposure.include=prometheus",
      "management.endpoints.web.path-mapping.prometheus=metrics"
    })
class MetricsEndpointIntegrationTest {
  @LocalServerPort private int port;

  private RestTestClient restTestClient;

  @BeforeEach
  void setUp() {
    restTestClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
  }

  @Test
  void hikariAndHibernateMetricsAreServedThroughTheMetricsEndpoint() {
    String body =
        restTestClient
            .get()
            .uri("/actuator/metrics")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

    assertThat(body).contains("hikaricp_connections_max");
    assertThat(body).contains("hibernate_sessions_open");
  }
}
