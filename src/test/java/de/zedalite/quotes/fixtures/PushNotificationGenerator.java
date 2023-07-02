package de.zedalite.quotes.fixtures;

import de.zedalite.quotes.data.model.PushNotification;

public class PushNotificationGenerator {

  public static PushNotification getPushNotification() {
    return new PushNotification("Notification", "Sale!");
  }
}
