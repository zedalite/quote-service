package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record QuoteOfTheDayRequest(

  @NonNull
  @PositiveOrZero
  Integer quoteId,

  @NonNull
  @PastOrPresent
  LocalDate creationDate

) {
}
