package de.zedproject.quotesapi.exceptions;

public class QotdNotFoundException extends RuntimeException {

  public QotdNotFoundException(final String message) {
    super(message);
  }
}
