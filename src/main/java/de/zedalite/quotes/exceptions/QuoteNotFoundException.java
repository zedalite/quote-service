package de.zedalite.quotes.exceptions;

public class QuoteNotFoundException extends RuntimeException {

  public QuoteNotFoundException(final String message) {
    super(message);
  }
}
