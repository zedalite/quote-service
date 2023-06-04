package de.zedproject.quotesapi.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import de.zedproject.quotesapi.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService tokenService;

  private final UserDetailsService userDetailsService;


  public JwtAuthenticationFilter(final JwtTokenService tokenService, UserDetailsService userDetailsService) {
    this.tokenService = tokenService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final var header = request.getHeader(AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final var token = header.substring(7);
    String username;
    try {
      username = tokenService.validateToken(token);
      // TODO logging for security opations?, e.g. unauthorized
    } catch (JWTVerificationException ex) {
      filterChain.doFilter(request, response);
      return;
    }

    final var userDetails = userDetailsService.loadUserByUsername(username);
    final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
