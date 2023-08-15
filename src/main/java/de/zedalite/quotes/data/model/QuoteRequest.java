package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
public record QuoteRequest(

  @NotBlank
  @Size(max = 32)
  String author,

  @NotNull
  @PastOrPresent
  LocalDateTime creationDate,

  @NotBlank
  @Size(max = 256)
  String text,

  @Size(max = 64)
  String context,

  @PositiveOrZero
  Integer creatorId

) {

  public QuoteRequest withCreatorId(final Integer creatorId) {
    return new QuoteRequest(author, creationDate, text, context, creatorId);

  }
}
