package com.stano.domain_jpa;

import com.stano.domain_jpa.springdata.RoutingRepositoryFactoryBean;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Enables the platform's JPA support on a Spring configuration class: entity scanning, JPA
 * repositories backed by {@link RoutingRepositoryFactoryBean}, and the defaults in {@link
 * DefaultJpaSpringConfig}.
 *
 * <p>Add this annotation to a {@code @Configuration} (or Spring Boot application) class to opt a
 * module into JPA. By default, entities are scanned from the annotated class's own package; use
 * {@link #entityPackages()} to scan additional packages.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({EnableJpaRegistrar.class, DefaultJpaSpringConfig.class})
@EnableJpaRepositories(repositoryFactoryBeanClass = RoutingRepositoryFactoryBean.class)
public @interface EnableJpa {
  /**
   * Marker classes whose packages should be scanned for JPA entities, in addition to the package
   * containing the converters used by this module. When empty (the default), the package of the
   * class annotated with {@code @EnableJpa} is scanned instead.
   *
   * @return marker classes identifying the packages to scan for entities
   */
  Class<?>[] entityPackages() default {};
}
