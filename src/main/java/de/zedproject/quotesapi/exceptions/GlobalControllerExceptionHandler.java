package de.zedproject.quotesapi.exceptions;

import de.zedproject.quotesapi.data.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDetails handleNotFoundException(final ResourceNotFoundException ex) {
    return new ErrorDetails(LocalDateTime.now(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDetails handleNotValidException(final MethodArgumentNotValidException ex) {
    final var validationErrors = ex.getDetailMessageArguments();
    if (validationErrors != null && validationErrors.length > 0) {
      final var details = Arrays.stream(validationErrors)
              .skip(1).map(Object::toString)
              .collect(Collectors.joining(System.lineSeparator()));
      return new ErrorDetails(LocalDateTime.now(), "Validation failed", details);

    }
    return new ErrorDetails(LocalDateTime.now(), "Validation failed");
  }
}
