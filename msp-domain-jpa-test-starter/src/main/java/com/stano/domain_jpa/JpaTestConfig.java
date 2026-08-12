package com.stano.domain_jpa;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.TestPropertySource;

/**
 * Boot configuration root picked up by {@code @DataJpaTest}/{@link JpaTest} slice tests: enables
 * JPA via {@code @EnableJpa} and allows bean definitions to be overridden so test-specific beans
 * (such as the embedded datasource in {@link PostgresJpaTestConfig}) can replace defaults.
 */
@SpringBootConfiguration
@EnableJpa
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class JpaTestConfig {}
