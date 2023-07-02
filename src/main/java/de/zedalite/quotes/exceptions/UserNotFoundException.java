package de.zedalite.quotes.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(final String message) {
    super(message);
  }
}
