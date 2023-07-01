package de.zedproject.quotesapi.fixtures;

import de.zedproject.quotesapi.data.model.PushNotification;

public class PushNotificationGenerator {

  public static PushNotification getPushNotification() {
    return new PushNotification("Notification", "Sale!");
  }
}
