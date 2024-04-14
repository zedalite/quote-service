package de.zedalite.quotes.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService tokenService;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(final JwtTokenService tokenService, final UserDetailsService userDetailsService) {
    this.tokenService = tokenService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    @NotNull final HttpServletRequest request,
    @NotNull final HttpServletResponse response,
    @NotNull final FilterChain filterChain
  ) throws ServletException, IOException {
    final String header = request.getHeader(AUTHORIZATION);

    if (header != null && header.startsWith("Bearer ")) {
      final String token = header.substring(7); // cut "Bearer "

      try {
        final String username = tokenService.validateToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (final JWTVerificationException ex) {
        filterChain.doFilter(request, response);
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
