package de.zedproject.quotesapi.exceptions;

import de.zedproject.quotesapi.data.model.ErrorDetails;
import de.zedproject.quotesapi.data.model.ValidationErrorDetails;
import de.zedproject.quotesapi.data.model.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDetails handleNotFoundException(final ResourceNotFoundException ex) {
    return new ErrorDetails(LocalDateTime.now(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorDetails handleNotValidException(final MethodArgumentNotValidException ex) {
    final List<Violation> violations = new ArrayList<>();
    final var details = new ValidationErrorDetails(LocalDateTime.now(), "Validation failed", violations);
    for (final var fieldError : ex.getBindingResult().getFieldErrors()) {
      details.violations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    return details;
  }
}
