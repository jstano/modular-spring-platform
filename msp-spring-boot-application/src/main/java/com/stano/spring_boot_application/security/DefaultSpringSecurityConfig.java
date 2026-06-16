package com.stano.spring_boot_application.security;

import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@AutoConfiguration
public class DefaultSpringSecurityConfig {
  @Bean
  @ConditionalOnMissingBean(SecurityFilterChain.class)
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, List<Customizer<HttpSecurity>> customizers) {
    for (Customizer<HttpSecurity> customizer : customizers) {
      customizer.customize(http);
    }

    // Enables cookie-based session handling
    http.securityContext(context -> context.requireExplicitSave(false));

    // Enables bearer token support — requires spring.security.oauth2.resourceserver.jwt.issuer-uri
    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

    return http.build();
  }

  @Bean
  @ConditionalOnMissingBean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(false);
    config.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  @Order(10)
  public Customizer<HttpSecurity> corsSecurityCustomizer(CorsConfigurationSource corsSource) {
    return http -> http.cors(cors -> cors.configurationSource(corsSource));
  }

  @Bean
  @Order(20)
  public Customizer<HttpSecurity> csrfSecurityCustomizer() {
    return http -> http.csrf(AbstractHttpConfigurer::disable);
  }

  @Bean
  @Order(30)
  public Customizer<HttpSecurity> actuatorSecurityCustomizer() {
    return http ->
        http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/health", "/health/**")
                    .permitAll()
                    .requestMatchers("/metrics")
                    .permitAll()
                    .requestMatchers("/info")
                    .permitAll());
  }
}
