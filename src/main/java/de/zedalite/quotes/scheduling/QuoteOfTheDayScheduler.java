package de.zedalite.quotes.scheduling;

import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.exceptions.NotifierException;
import de.zedalite.quotes.exceptions.QuoteNotFoundException;
import de.zedalite.quotes.exceptions.ResourceNotFoundException;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * The QuoteOfTheDayScheduler class is responsible for scheduling the reset of the quote of the day
 * and sending push notifications.
 */
@Component
public class QuoteOfTheDayScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteOfTheDayScheduler.class);

  private final QuoteService quoteService;

  private final PushNotificationRepository notifierRepository;

  @Value("${notification.topic.quote-of-the-day}")
  private String qotdTopic;

  /**
   * Initializes a new instance of the QuoteOfTheDayScheduler class.
   *
   * @param quoteService       the QuoteService used to retrieve the quote of the day
   * @param notifierRepository the PushNotificationRepository used to send notification
   */
  public QuoteOfTheDayScheduler(final QuoteService quoteService, final PushNotificationRepository notifierRepository) {
    this.quoteService = quoteService;
    this.notifierRepository = notifierRepository;
  }

  /**
   * Clears the cache for the quote of the day and retrieves a new quote from the quote service.
   * This method is scheduled to run daily.
   */
  @Scheduled(cron = "@daily")
  public void resetQuoteOfTheDay() {
    emptyQotdCache();
    quoteService.findQuoteOfTheDay();
  }


  /**
   * Sends a push notification for the quote of the day.
   * This method is scheduled to run every day at 12:00 PM.
   */
  @Scheduled(cron = "0 0 12 * * *")
  public void sendPushNotification() {
    try {
      final var quoteId = quoteService.findQuoteOfTheDay().id();

      final var notification = new PushNotification(
        "Quote Of The Day",
        "Check out the inspiration for today",
        Map.of("type", "QOTD", "quoteId", String.valueOf(quoteId))
      );

      notifierRepository.sendToTopic(qotdTopic, notification);
      LOGGER.info("PushNotification for qotd sent, topic={}, quoteId={}", qotdTopic, quoteId);
    } catch (final QuoteNotFoundException | ResourceNotFoundException ex) {
      LOGGER.warn("PushNotification for qotd failed, no or not enough quote(s) found");
    } catch (final NotifierException ex) {
      LOGGER.warn("PushNotification for qotd failed, topic={}.", qotdTopic);
    }
  }

  /**
   * Empties the cache for the Quote of the Day.
   */
  @CacheEvict(value = "qotd", allEntries = true)
  public void emptyQotdCache() {
    LOGGER.info("Cache QOTD emptied");
  }
}
