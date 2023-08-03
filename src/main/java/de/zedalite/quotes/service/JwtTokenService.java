package de.zedalite.quotes.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * JwtTokenService class handles the generation and validation of JSON Web Tokens (JWT).
 */
@Service
public class JwtTokenService {

  private final Algorithm hmac512;

  private final Long tokenValidityTime;

  /**
   * JwtTokenService class handles the generation and validation of JSON Web Tokens (JWT).
   *
   * @param secret              The secret key used for token signing and verification.
   * @param tokenValidityTime   The token validity time in milliseconds.
   */
  public JwtTokenService(@Value("${auth.jwt.secret}") final String secret, @Value("${auth.jwt.validity}") final Long tokenValidityTime) {
    this.tokenValidityTime = tokenValidityTime;
    this.hmac512 = Algorithm.HMAC512(secret);
  }

  /**
   * Generates a JSON Web Token (JWT) for the given username.
   *
   * @param username   The username for whom the token is generated.
   * @return The generated JWT as a string.
   */
  public String generateToken(final String username) {
    final Instant now = Instant.now();
    return JWT.create()
      .withSubject(username)
      .withIssuer("quote-api")
      .withIssuedAt(now)
      .withExpiresAt(now.plusMillis(tokenValidityTime * 1000L))
      .sign(hmac512);
  }

  /**
   * Validates a JSON Web Token (JWT) and returns the subject (username) if the token is valid.
   *
   * @param token   The token to be validated.
   * @return The username (subject) extracted from the validated token.
   */
  public String validateToken(final String token) {
    return JWT.require(hmac512)
      .withClaimPresence("sub")
      .withIssuer("quote-api")
      .build()
      .verify(token)
      .getSubject();
  }
}
