package com.stano.spring_security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@AutoConfiguration
@PropertySource("classpath:spring-security-test-defaults.properties")
public class SpringSecurityTestAutoConfiguration {
  @Bean
  JwtDecoder jwtDecoder() {
    return token -> {
      throw new UnsupportedOperationException("JWT not used in integration tests");
    };
  }
}
