package de.zedalite.quotes.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.zedalite.quotes.data.model.ErrorResponse;
import de.zedalite.quotes.data.model.ValidationErrorDetails;
import de.zedalite.quotes.data.model.Violation;
import de.zedalite.quotes.web.GlobalControllerExceptionHandler;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalControllerExceptionHandlerTest {

  private final GlobalControllerExceptionHandler instance = new GlobalControllerExceptionHandler();

  @Test
  @DisplayName("Should handle ResourceNotFoundException")
  void shouldHandleResourceNotFoundException() {
    final ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

    final ErrorResponse errorResponse = instance.handleNotFoundException(exception).getBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.message()).isEqualTo("Resource not found");
  }

  @ParameterizedTest
  @ValueSource(classes = { ResourceAlreadyExitsException.class, ResourceAccessException.class })
  @DisplayName("Should handle ResourceAccessException")
  void shouldHandleResourceAccessException(final Class<? extends RuntimeException> clazz) throws Exception {
    final RuntimeException exception = clazz
      .getDeclaredConstructor(String.class)
      .newInstance("Resource can't be accessed");

    final ErrorResponse errorResponse = instance.handleForbiddenException(exception).getBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.message()).isEqualTo("Resource can't be accessed");
  }

  @Test
  @DisplayName("Should handle ValidationException")
  void shouldHandleValidationException() {
    final MethodParameter methodParameter = mock(MethodParameter.class);
    final BindingResult bindingResult = mock(BindingResult.class);
    final FieldError fieldError = mock(FieldError.class);

    given(fieldError.getField()).willReturn("name");
    given(fieldError.getDefaultMessage()).willReturn("size must be between 0 and 32");
    given(bindingResult.getFieldErrors()).willReturn(List.of(fieldError));

    final MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
      methodParameter,
      bindingResult
    );

    final ValidationErrorDetails errorDetails = instance.handleNotValidException(exception).getBody();

    assertThat(errorDetails).isNotNull();
    assertThat(errorDetails.violations()).containsOnly(new Violation("name", "size must be between 0 and 32"));
  }
}
