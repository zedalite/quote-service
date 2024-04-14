package de.zedalite.quotes.security;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenService {

  private final JwkProvider jwkProvider;

  @Value("${app.security.jwt.issuer}")
  private List<String> jwtIssuer;

  public JwtTokenService(final @Value("${app.security.jwk.url}") URL jwkHost) {
    this.jwkProvider = new JwkProviderBuilder(jwkHost)
      .cached(10, 24, TimeUnit.HOURS)
      .build();
  }

  public String validateToken(final String token) {
    final JWTVerifier verifier = JWT.require(Algorithm.RSA256(getKeyProvider()))
      .withIssuer(jwtIssuer.toArray(new String[0]))
      .withClaimPresence("name")
      .withClaimPresence("email")
      .build();

    final DecodedJWT jwt = verifier.verify(token);

    return jwt.getClaim("name").asString();
  }

  private RSAKeyProvider getKeyProvider() {
    return new RSAKeyProvider() {
      @Override
      public RSAPublicKey getPublicKeyById(final String keyId) {
        try {
          return (RSAPublicKey) jwkProvider.get(keyId).getPublicKey();
        } catch (JwkException e) {
          throw new JWTDecodeException(e.getMessage());
        }
      }

      @Override
      public RSAPrivateKey getPrivateKey() {
        return null;
      }

      @Override
      public String getPrivateKeyId() {
        return "";
      }
    };
  }



}
