package de.zedalite.quotes.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;

@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record Violation(
  @NotNull String field,

  @NotNull String message
) {}
