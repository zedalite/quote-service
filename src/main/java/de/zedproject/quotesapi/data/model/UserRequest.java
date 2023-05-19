package de.zedproject.quotesapi.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record UserRequest(

   @NotBlank
   String name,

   @NotBlank
   String password

) {
    public UserRequest withPassword(final String password) {
        return new UserRequest(name, password);
    }
}
