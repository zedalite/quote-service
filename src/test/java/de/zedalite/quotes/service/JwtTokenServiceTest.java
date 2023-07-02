package de.zedalite.quotes.service;

import com.auth0.jwt.JWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

  private final JwtTokenService instance = new JwtTokenService("secret", 10L);

  @Test
  @DisplayName("Should generate token")
  void shouldGenerateToken() {
    final var token = instance.generateToken("quoter");

    assertThat(token).isNotNull();
    assertThat(JWT.decode(token).getSubject()).isEqualTo("quoter");
    assertThat(JWT.decode(token).getIssuer()).isEqualTo("quote-api");
  }

  @Test
  @DisplayName("Should validate token")
  void shouldValidateToken() {
    final var token = instance.generateToken("quoter");
    final var user = instance.validateToken(token);

    assertThat(user).isEqualTo("quoter");
  }

  //TODO test for invalid and expired token
}