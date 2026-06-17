package com.stano.domain_jpa;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootConfiguration
@EnableJpa
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class JpaTestConfig {}
