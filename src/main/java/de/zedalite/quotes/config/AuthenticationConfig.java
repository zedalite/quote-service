package de.zedalite.quotes.config;

import de.zedalite.quotes.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class AuthenticationConfig {

  private final AuthenticationService authenticationService;

  public AuthenticationConfig(final AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return authenticationService::getUser;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    return authProvider;
  }

  // TODO Global AuthenticationManager configured with an AuthenticationProvider bean. UserDetailsService beans will not be used for username/password login. Consider removing the AuthenticationProvider bean. Alternatively, consider using the UserDetailsService in a manually instantiated DaoAuthenticationProvider.

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
