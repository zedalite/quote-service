package de.zedalite.quotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceAlreadyExitsException extends RuntimeException {

  public ResourceAlreadyExitsException(final String message) {
    super(message);
  }
}
