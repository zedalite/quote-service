package de.zedalite.quotes.scheduling;

import de.zedalite.quotes.exception.ResourceNotFoundException;
import de.zedalite.quotes.service.GroupQuoteOfTheDayService;
import de.zedalite.quotes.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final GroupQuoteOfTheDayService groupQuoteOfTheDayService;

  private final GroupService groupService;

  public QuoteOfTheDayScheduler(final GroupQuoteOfTheDayService groupQuoteOfTheDayService, final GroupService groupService) {
    this.groupQuoteOfTheDayService = groupQuoteOfTheDayService;
    this.groupService = groupService;
  }

  /**
   * Clears the cache for the quote of the day and retrieves a new quote from the quote service.
   * This method is scheduled to run daily.
   */
  @Scheduled(cron = "@daily")
  public void resetQuoteOfTheDay() {
    LOGGER.info("Resetting quotes of the day...");
    emptyQotdCache();

    for (final Integer groupId : groupService.findAllIds()) {
      try {
        groupQuoteOfTheDayService.findQuoteOfTheDay(groupId);
      } catch (final ResourceNotFoundException ignored) {
        LOGGER.warn("Quotes of the day reset not reset, groupId={}", groupId);
      }
    }
    LOGGER.info("Quotes of the day reset.");

  }

  /**
   * Empties the cache for the Quote of the Day.
   */
  @CacheEvict(value = "qotd", allEntries = true)
  public void emptyQotdCache() {
    LOGGER.info("Cache QOTD emptied");
  }
}
