package de.zedproject.quotesapi.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record ErrorDetails(
        @NotNull @PastOrPresent LocalDateTime timestamp,
        @NotNull String message,
        String details
) {
  public ErrorDetails(final LocalDateTime time, final String message) {
    this(time, message, null);
  }
}
