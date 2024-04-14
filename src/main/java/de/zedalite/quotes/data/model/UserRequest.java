package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record UserRequest(

  @NotBlank
  @Size(max = 32)
  String name,

  @NotBlank
  @Size(max = 64)
  String email,

  @NotBlank
  @Size(max = 32)
  String displayName

) {

  public UserRequest(final String name, final String email) {
    this(name, email, name);
  }
}
