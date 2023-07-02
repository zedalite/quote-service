package de.zedalite.quotes.repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.exceptions.NotifierException;
import org.springframework.stereotype.Repository;

@Repository
public class PushNotificationRepository {

  private final FirebaseMessaging fcm;

  public PushNotificationRepository(final FirebaseMessaging fcm) {
    this.fcm = fcm;
  }

  public void sendToTopic(final String topic, final PushNotification pushNotification) throws NotifierException {
    final var notification = Notification.builder()
      .setTitle(pushNotification.title())
      .setBody(pushNotification.body())
      .build();

    final var message = Message.builder()
      .setTopic(topic)
      .setNotification(notification)
      .build();

    try {
      fcm.send(message);
    } catch (final FirebaseMessagingException e) {
      throw new NotifierException(e.getMessage());
    }
  }
}
