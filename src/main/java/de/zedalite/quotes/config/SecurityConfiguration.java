package de.zedalite.quotes.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import de.zedalite.quotes.security.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;

  private final AuthenticationProvider authenticationProvider;

  private final List<String> corsAllowedOrigins;

  public SecurityConfiguration(
    final JwtAuthenticationFilter jwtAuthFilter,
    final AuthenticationProvider authenticationProvider,
    final @Value("${app.security.cors.allowedOrigins}") List<String> corsAllowedOrigins
  ) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.authenticationProvider = authenticationProvider;
    this.corsAllowedOrigins = corsAllowedOrigins;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http
      .cors(c -> c.configurationSource(getCorsConfiguration()))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authz -> authz.requestMatchers("/actuator/*").permitAll().anyRequest().authenticated())
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  private CorsConfigurationSource getCorsConfiguration() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(corsAllowedOrigins);

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
