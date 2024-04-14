package de.zedalite.quotes.security;

import de.zedalite.quotes.data.mapper.RequestMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "RequestLogger", urlPatterns = "/*")
public class RequestLogger extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogger.class);

  private static final RequestMapper REQUEST_MAPPER = RequestMapper.INSTANCE;

  @Override
  protected void doFilterInternal(
    @NotNull final HttpServletRequest request,
    @NotNull final HttpServletResponse response,
    final FilterChain filterChain
  ) throws ServletException, IOException {
    final Instant start = Instant.now();
    try {
      filterChain.doFilter(request, response);
    } finally {
      final Instant finish = Instant.now();
      final long time = Duration.between(start, finish).toMillis();

      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      final String username = (authentication != null) ? authentication.getName() : "anonymous";

      final String logMessage = REQUEST_MAPPER.map(request, response.getStatus(), username, time).toString();

      LOGGER.info(logMessage);
    }
  }
}
