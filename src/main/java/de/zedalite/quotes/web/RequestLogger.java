package de.zedalite.quotes.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
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
  protected void doFilterInternal(final @NotNull HttpServletRequest request, final @NotNull HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    final Instant start = Instant.now();
    try {
      filterChain.doFilter(request, response);
    } finally {
      final Instant finish = Instant.now();
      final long time = Duration.between(start, finish).toMillis();

      final String type = request.getMethod();
      final String uri = request.getRequestURI();
      String query = request.getQueryString();
      query = query != null && !query.isBlank() ? "?" + query : "";
      final int status = response.getStatus();
      final String client = Objects.requireNonNullElse(request.getHeader("X-Forwarded-For"), request.getRemoteAddr());
      final String user = SecurityContextHolder.getContext().getAuthentication().getName();

      LOGGER.info("request [{} {}{}, status={}, client={}, user={}, duration={}ms]", type, uri, query, status, client, user, time);
    }
  }
}
