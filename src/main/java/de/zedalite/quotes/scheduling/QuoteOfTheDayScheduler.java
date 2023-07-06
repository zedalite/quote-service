package de.zedalite.quotes.scheduling;

import de.zedalite.quotes.data.model.PushNotification;
import de.zedalite.quotes.repository.PushNotificationRepository;
import de.zedalite.quotes.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
   * @param quoteService         the QuoteService used to retrieve the quote of the day
   * @param notifierRepository  the PushNotificationRepository used to send notification
   */
  public QuoteOfTheDayScheduler(final QuoteService quoteService, final PushNotificationRepository notifierRepository) {
    this.quoteService = quoteService;
    this.notifierRepository = notifierRepository;
  }

  /**
   * Resets the quote of the day by emptying the cache, retrieving a new quote from the QuoteService,
   * and sending a push notification using the PushNotificationRepository.
   * This method is scheduled to run every day at 8:00 AM.
   */
  @Scheduled(cron = "0 8 * * * *")
  public void resetQuoteOfTheDay() {
    emptyQotdCache();
    quoteService.findQuoteOfTheDay();
    sendPushNotification();
  }

  /**
   * Empties the cache for the Quote of the Day.
   */
  @CacheEvict(value = "qotd", allEntries = true)
  public void emptyQotdCache() {
    LOGGER.info("Cache QOTD emptied");
  }

  /**
   * Sends a push notification to the specified topic.
   */
  private void sendPushNotification() {
    final var notification = new PushNotification(
      "Quote Of The Day",
      "Check out the inspiration for today");
    notifierRepository.sendToTopic(qotdTopic, notification);
    LOGGER.info("Push notification sent to topic={}.", qotdTopic);
  }
}
