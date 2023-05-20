package de.zedproject.quotesapi.exceptions;

public class QuoteNotFoundException extends RuntimeException {

  public QuoteNotFoundException(final String message) {
    super(message);
  }
}
