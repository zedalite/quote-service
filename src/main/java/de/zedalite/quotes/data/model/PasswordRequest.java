package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record PasswordRequest(

  @NotBlank
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]).{8,128}$", message = "must contain letters and numbers with length between 8-128")
  String password

) {
}
