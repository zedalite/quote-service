package de.zedproject.quotesapi.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Component
public class RequestLogger extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogger.class);

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    final var start = Instant.now();
    try {
      filterChain.doFilter(request, response);
    } finally {
      final var finish = Instant.now();
      final long time = Duration.between(start, finish).toMillis();

      final var type = request.getMethod();
      final var uri = request.getRequestURI();
      final var query = Objects.requireNonNullElse(request.getQueryString(), "");
      final var status = response.getStatus();
      final var client = Objects.requireNonNullElse(request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
      final var user = SecurityContextHolder.getContext().getAuthentication().getName();

      LOGGER.info("request [{} {}{}, status={}, client={}, user={}, duration={}ms]", type, uri, query, status, client, user, time);
    }
  }
}
