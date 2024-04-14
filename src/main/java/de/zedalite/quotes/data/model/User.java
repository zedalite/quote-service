package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record User(
  @NotNull
  @PositiveOrZero
  Integer id,

  @NotBlank
  @Size(max = 32)
  String name,

  @NotBlank
  @Size(max = 64)
  String email,

  @NotBlank
  @Size(max = 32)
  String displayName,

  @NotNull
  @PastOrPresent
  LocalDateTime creationDate

) {
}
