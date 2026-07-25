package com.stano.domain_jpa.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TestConfig.class)
@TestPropertySource(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      "msp.schema.auto-install=true",
      "spring.jpa.properties.hibernate.generate_statistics=true"
    })
class HibernateMetricsIntegrationTest {
  @Autowired private MeterRegistry meterRegistry;

  @Test
  void hibernateStatisticsMetricsAreRegisteredForTheEntityManagerFactoryBean() {
    assertThat(meterRegistry.find("hibernate.sessions.open").meter()).isNotNull();
    assertThat(meterRegistry.find("hibernate.statements").meter()).isNotNull();
  }
}
