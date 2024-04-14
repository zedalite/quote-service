package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record ValidationErrorDetails(
  @NotNull @PastOrPresent LocalDateTime timestamp,

  @NotNull String message,

  List<Violation> violations
) {}
