package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record QuoteOfTheDay(

  @NotNull
  @PositiveOrZero
  Integer id,

  @NonNull
  @PositiveOrZero
  Integer quoteId,

  @NonNull
  @PastOrPresent
  LocalDateTime creationDate

) {
}
