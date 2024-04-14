package de.zedalite.quotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceAccessException extends RuntimeException {

  public ResourceAccessException(final String message) {
    super(message);
  }
}
