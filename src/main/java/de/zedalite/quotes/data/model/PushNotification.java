package de.zedalite.quotes.data.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

public record PushNotification(
  @NotBlank String title,

  @NotBlank String body,

  Map<String, String> data
) {
  public PushNotification(final String title, final String body) {
    this(title, body, Collections.emptyMap());
  }
}
