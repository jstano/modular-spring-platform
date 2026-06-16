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

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({EnableJpaRegistrar.class, DefaultJpaSpringConfig.class})
@EnableJpaRepositories(repositoryFactoryBeanClass = RoutingRepositoryFactoryBean.class)
public @interface EnableJpa {
  Class<?>[] entityPackages() default {};
}
