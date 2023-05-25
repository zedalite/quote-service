package de.zedproject.quotesapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtTokenService {
  private final Algorithm hmac512;
  private final Long tokenValidityTime;

  public JwtTokenService(@Value("${auth.jwt.secret}") final String secret, @Value("${auth.jwt.validity}") final Long tokenValidityTime) {
    this.tokenValidityTime = tokenValidityTime;
    this.hmac512 = Algorithm.HMAC512(secret);
  }

  public String generateToken(final String username) {
    final Instant now = Instant.now();
    return JWT.create()
      .withSubject(username)
      .withIssuer("quote-api")
      .withIssuedAt(now)
      .withExpiresAt(now.plusMillis(tokenValidityTime * 1000L))
      .sign(hmac512);
  }

  public String validateToken(final String token) {
    return JWT.require(hmac512)
      .withClaimPresence("sub")
      .build()
      .verify(token)
      .getSubject();
  }
}
