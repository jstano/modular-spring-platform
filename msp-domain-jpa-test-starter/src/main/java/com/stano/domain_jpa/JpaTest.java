package com.stano.domain_jpa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

/**
 * Meta-annotation for JPA slice tests: composes {@code @DataJpaTest} with
 * {@code @AutoConfigureTestDatabase(replace = Replace.NONE)} so the module's own test datasource
 * configuration (see {@link PostgresJpaTestConfig}) is used instead of Spring Boot's default
 * embedded H2 replacement.
 *
 * <p>Named {@code @JpaTest} rather than {@code @EnableJpaTest} per project convention.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface JpaTest {}
