package de.zedalite.quotes.repository;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import de.zedalite.quotes.exceptions.NotifierException;
import de.zedalite.quotes.fixtures.PushNotificationGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class PushNotificationRepositoryTest {

  @InjectMocks
  private PushNotificationRepository instance;

  @Mock
  private FirebaseMessaging firebaseMessaging;

  @Test
  @DisplayName("Should send to topic")
  void shouldSendToTopic() throws Exception {
    final var notification = PushNotificationGenerator.getPushNotification();

    instance.sendToTopic("test", notification);

    then(firebaseMessaging).should().send(any());
  }

  @Test
  @DisplayName("Should throw exception when sending fails")
  void shouldThrowExceptionWhenSendingFails() throws Exception {
    final var notification = PushNotificationGenerator.getPushNotification();
    willThrow(FirebaseMessagingException.class).given(firebaseMessaging).send(any(Message.class));

    assertThatCode(() -> instance.sendToTopic("test", notification)).isInstanceOf(NotifierException.class);
  }
}
