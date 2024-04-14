package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QuoteMessage(
  @NotNull @PositiveOrZero Integer id,
  @NotBlank @Size(max = 32) String author,

  @NotNull @PastOrPresent LocalDateTime creationDate,

  @NotBlank @Size(max = 256) String text,

  @Size(max = 64) String context,

  @PositiveOrZero Integer creatorId,

  List<User> mentions
) {
  public String truncateText() {
    return truncateText(128);
  }

  public String truncateText(final Integer maxLength) {
    return this.text().substring(0, Math.min(this.text().length(), maxLength));
  }
}
