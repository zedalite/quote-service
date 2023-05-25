package de.zedproject.quotesapi.data.model;

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
  @Size(max = 128)
  String password

) {
  public UserRequest withPassword(final String password) {
    return new UserRequest(name, password);
  }
}
