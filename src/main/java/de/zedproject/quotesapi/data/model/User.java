package de.zedproject.quotesapi.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;

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
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,128}$", message = "must contain letters, numbers and special chars with length between 8-128")
  String password

) {
}
