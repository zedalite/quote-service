package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record UserRequest(

  @NotBlank
  @Size(max = 32)
  String name,

  @NotBlank
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]).{8,128}$", message = "must contain letters and numbers with length between 8-128")
  String password,

  @NotBlank //TODO enforce not blank after app update + enforce not null on database
  @Size(max = 32)
  String displayName

) {

  public UserRequest(final String name, final String password) {
    this(name, password, name);
  }

  public UserRequest withPassword(final String password) {
    return new UserRequest(name, password, displayName);
  }
}
