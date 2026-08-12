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

/**
 * Auto-configuration providing a baseline Spring Security setup for platform applications: CORS,
 * CSRF, actuator endpoint access, and OAuth2 resource server (JWT) support.
 *
 * <p>Rather than overriding the whole {@link SecurityFilterChain}, applications can contribute
 * additional {@code Customizer<HttpSecurity>} beans (ordered with {@link Order}) which are applied
 * in order before the platform's own authorization rules.
 */
@AutoConfiguration
public class DefaultSpringSecurityConfig {
  /**
   * Builds the platform's default {@link SecurityFilterChain}, applying every {@code
   * Customizer<HttpSecurity>} bean in the context (in {@link Order} order), enabling cookie-based
   * session handling and OAuth2 resource server JWT support, and requiring authentication for any
   * request not otherwise permitted by a customizer.
   *
   * <p>Only active if the application has not defined its own {@code SecurityFilterChain} bean.
   *
   * @param http the {@code HttpSecurity} builder to configure
   * @param customizers all {@code Customizer<HttpSecurity>} beans in the context, applied in order
   * @return the built security filter chain
   */
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

  /**
   * Provides a permissive default CORS configuration (all origins, standard HTTP methods, all
   * headers, no credentials) applied to every path, unless the application has already defined a
   * {@code CorsConfigurationSource} bean.
   *
   * @return the default CORS configuration source
   */
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

  /**
   * Enables CORS on the security filter chain using the given {@link CorsConfigurationSource}.
   * Applied first among the platform's own customizers.
   *
   * @param corsSource the CORS configuration source to apply
   * @return a customizer that enables CORS support
   */
  @Bean
  @Order(10)
  public Customizer<HttpSecurity> corsSecurityCustomizer(CorsConfigurationSource corsSource) {
    return http -> http.cors(cors -> cors.configurationSource(corsSource));
  }

  /**
   * Disables CSRF protection, appropriate for stateless, token-authenticated APIs.
   *
   * @return a customizer that disables CSRF protection
   */
  @Bean
  @Order(20)
  public Customizer<HttpSecurity> csrfSecurityCustomizer() {
    return http -> http.csrf(AbstractHttpConfigurer::disable);
  }

  /**
   * Permits unauthenticated access to the platform's actuator endpoints ({@code /health}, {@code
   * /metrics}, {@code /info}).
   *
   * @return a customizer that permits access to actuator endpoints
   */
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
