package com.stano.domain_jpa;

import com.stano.domain_jpa.jpa.hibernate.TraceIdStatementInspector;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Default JPA configuration imported by {@link EnableJpa}: enables Spring Data JPA auditing
 * (populating {@code @CreatedDate}/{@code @LastModifiedDate} fields) and declarative transaction
 * management, and supplies platform-wide Hibernate defaults.
 *
 * <p>This class is imported automatically via {@code @EnableJpa} and is not normally referenced
 * directly by application code.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class DefaultJpaSpringConfig {
  /**
   * Supplies the platform's default Hibernate properties: UTC session time zone and the {@link
   * TraceIdStatementInspector} used to tag SQL statements with the current trace id. Existing
   * properties set elsewhere are left untouched, since only {@code putIfAbsent} is used.
   *
   * @return a customizer applying the platform's default Hibernate properties
   */
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
