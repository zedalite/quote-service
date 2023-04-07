package de.zedproject.quotesapi.data.payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = QuoteRequest.class, builder = ImmutableQuoteRequest.Builder.class)
public interface QuoteRequest {
  String author();

  LocalDateTime datetime();

  String text();

  Optional<String> subtext();
}
