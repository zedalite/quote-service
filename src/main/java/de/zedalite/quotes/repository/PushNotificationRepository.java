package de.zedalite.quotes.repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.exception.NotifierException;
import org.springframework.stereotype.Repository;

/**
 * Repository class for sending push notifications to specific topics.
 * Uses FirebaseMessaging to send the push notifications.
 */
@Repository
public class PushNotificationRepository {

  private final FirebaseMessaging fcm;

  public PushNotificationRepository(final FirebaseMessaging fcm) {
    this.fcm = fcm;
  }

  /**
   * Sends a push notification to a specific topic.
   *
   * @param topic            the topic to which the push notification will be sent
   * @param pushNotification the push notification to send
   * @throws NotifierException if an error occurs while sending the push notification
   */
  public void sendToTopic(final String topic, final PushNotification pushNotification) throws NotifierException {
    final Notification notification = Notification.builder()
      .setTitle(pushNotification.title())
      .setBody(pushNotification.body())
      .build();

    final Message message = Message.builder()
      .setTopic(topic)
      .setNotification(notification)
      .putAllData(pushNotification.data())
      .build();

    try {
      fcm.send(message);
    } catch (final FirebaseMessagingException e) {
      throw new NotifierException(e.getMessage());
    }
  }
}
