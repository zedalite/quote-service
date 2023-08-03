package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record Quote(

  @NotNull
  @PositiveOrZero
  Integer id,
  @NotBlank
  @Size(max = 32)
  String author,

  @NotNull
  @PastOrPresent
  LocalDateTime datetime,

  @NotBlank
  @Size(max = 256)
  String text,

  @Size(max = 64)
  String subtext,

  @PositiveOrZero
  Integer creatorId

) {

  public String truncateText() {
    return truncateText(128);
  }

  public String truncateText(final Integer maxLength) {
    return this.text().substring(0, Math.min(this.text().length(), maxLength));
  }
}
