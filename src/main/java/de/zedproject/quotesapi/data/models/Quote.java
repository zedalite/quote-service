package de.zedproject.quotesapi.data.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = Quote.class, builder = ImmutableQuote.Builder.class)
public interface Quote {
  Integer id();

  String author();

  LocalDateTime datetime();

  String text();

  Optional<String> subtext();
}
