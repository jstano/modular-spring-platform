package com.stano.domain_jpa;

import com.stano.domain_jpa.jpa.hibernate.TraceIdStatementInspector;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class DefaultJpaSpringConfig {
  @Bean
  public HibernatePropertiesCustomizer platformHibernateDefaults() {
    return properties -> {
      //      properties.putIfAbsent("hibernate.jdbc.batch_size", 200);
      //      properties.putIfAbsent("hibernate.order_inserts", true);
      properties.putIfAbsent("hibernate.jdbc.time_zone", "UTC");
      properties.putIfAbsent(
          "hibernate.session_factory.statement_inspector",
          TraceIdStatementInspector.class.getName());
    };
  }
}
