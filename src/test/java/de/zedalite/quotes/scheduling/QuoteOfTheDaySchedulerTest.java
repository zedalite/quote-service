package de.zedalite.quotes.scheduling;

import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import de.zedalite.quotes.fixtures.QuoteGenerator;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.service.QuoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteOfTheDaySchedulerTest {

  @InjectMocks
  private QuoteOfTheDayScheduler instance;

  @Mock
  private QuoteService quoteService;

  @Mock
  private PushNotificationRepository notifierRepository;

  @Test
  @DisplayName("Should reset quoteOfTheDay")
  void shouldResetQuoteOfTheDay() {
    instance.resetQuoteOfTheDay();

    then(quoteService).should().findQuoteOfTheDay();
  }

  @Test
  @DisplayName("Should send push notification")
  void shouldSendPushNotification() {
    final var qotd = QuoteGenerator.getQuoteMessage();
    willReturn(qotd).given(quoteService).findQuoteOfTheDay();

    instance.sendPushNotification();

    then(quoteService).should().findQuoteOfTheDay();
    then(notifierRepository).should().sendToTopic(any(), any(PushNotification.class));
  }

  @Test
  @DisplayName("Should not send notification when no qotd found")
  void shouldNotSendNotificationWhenNoQotdFound() {
    willThrow(QuoteNotFoundException.class).given(quoteService).findQuoteOfTheDay();

    instance.sendPushNotification();

    then(notifierRepository).shouldHaveNoInteractions();
  }
}
