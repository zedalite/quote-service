package de.zedproject.quotesapi.scheduling;

import de.zedproject.quotesapi.data.model.PushNotification;
import de.zedproject.quotesapi.repository.PushNotificationRepository;
import de.zedproject.quotesapi.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteOfTheDayScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteOfTheDayScheduler.class);

  private final QuoteService quoteService;

  private final PushNotificationRepository notifierRepository;

  @Value("{notification.topic.quote-of-the-day}")
  private String qotdTopic;

  public QuoteOfTheDayScheduler(final QuoteService quoteService, final PushNotificationRepository notifierRepository) {
    this.quoteService = quoteService;
    this.notifierRepository = notifierRepository;
  }

  @Scheduled(cron = "0 8 * * * *")
  public void resetQuoteOfTheDay() {
    emptyQotdCache();
    quoteService.findQuoteOfTheDay();
    sendPushNotification();
  }

  @CacheEvict(value = "qotd", allEntries = true)
  public void emptyQotdCache() {
    LOGGER.info("Cache QOTD emptied");
  }

  private void sendPushNotification() {
    final var notification = new PushNotification(
      "Quote Of The Day",
      "Check out the inspiration for today");
    notifierRepository.sendToTopic(qotdTopic, notification);
    LOGGER.info("Push notification sent to topic={}.", qotdTopic);
  }
}
