package com.stano.spring_security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * Auto-configuration for Spring Security integration tests: loads default test properties from
 * {@code spring-security-test-defaults.properties} and provides a stub {@link JwtDecoder} so a
 * resource-server security configuration can be wired up without a real OAuth2/JWT issuer being
 * available during tests.
 */
@AutoConfiguration
@PropertySource("classpath:spring-security-test-defaults.properties")
public class SpringSecurityTestAutoConfiguration {
  /**
   * Provides a stub {@link JwtDecoder} that always throws, since tests are expected to use {@code
   * spring-security-test}'s mock authentication support rather than decoding real JWTs.
   *
   * @return a decoder that unconditionally throws {@link UnsupportedOperationException}
   */
  @Bean
  JwtDecoder jwtDecoder() {
    return token -> {
      throw new UnsupportedOperationException("JWT not used in integration tests");
    };
  }
}
