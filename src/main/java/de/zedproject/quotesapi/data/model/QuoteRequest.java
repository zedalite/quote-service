package de.zedproject.quotesapi.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@JsonSerialize
@JsonDeserialize
public record QuoteRequest(

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
    String subtext

) {
}
