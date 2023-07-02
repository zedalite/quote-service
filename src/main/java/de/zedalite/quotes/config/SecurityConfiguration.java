package de.zedalite.quotes.config;

import de.zedalite.quotes.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;

  private final AuthenticationProvider authenticationProvider;

  public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.authenticationProvider = authenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http
      .cors(withDefaults())
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authz -> authz
        .requestMatchers("/auth/signup", "/auth/login", "/actuator/*").permitAll()
        .anyRequest().authenticated())
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authenticationProvider(authenticationProvider)
      .httpBasic(withDefaults())
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
