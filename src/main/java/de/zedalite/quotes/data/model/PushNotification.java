package de.zedalite.quotes.data.model;

import jakarta.validation.constraints.NotBlank;

public record PushNotification(

  @NotBlank
  String title,

  @NotBlank
  String body

) {
}
