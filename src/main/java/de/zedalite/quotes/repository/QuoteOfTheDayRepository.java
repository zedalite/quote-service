package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.QuotesOfTheDay;
import de.zedalite.quotes.data.jooq.tables.records.QuotesOfTheDayRecord;
import de.zedalite.quotes.data.mapper.QuoteOfTheDayMapper;
import de.zedalite.quotes.data.model.QuoteOfTheDay;
import de.zedalite.quotes.data.model.QuoteOfTheDayRequest;
import de.zedalite.quotes.exceptions.QotdNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * The QuoteOfTheDayRepository class is responsible for interacting with the database and
 * caching the Quote of the Day objects.
 * <p>
 * It provides methods for saving a Quote of the Day in the cache and retrieving a Quote
 * of the Day by date from the cache.
 */
@Repository
public class QuoteOfTheDayRepository {

  public static final QuotesOfTheDay QOTD = QuotesOfTheDay.QUOTES_OF_THE_DAY.as("QuotesOfTheDay");

  public static final QuoteOfTheDayMapper QOTD_MAPPER = QuoteOfTheDayMapper.INSTANCE;
  public static final String QOTD_NOT_FOUND = "QuoteOfTheDay not found";

  private final DSLContext dsl;

  public QuoteOfTheDayRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  /**
   * Saves a Quote of the Day in the cache and returns the saved QOTD object.
   *
   * @param request The QuoteOfTheDayRequest object containing the quote ID and date-time.
   * @return The saved QuoteOfTheDay object.
   * @throws QotdNotFoundException If the saved QOTD is null.
   */
  @CachePut(value = "qotd", key = "#request.creationDate().toLocalDate().atStartOfDay()", unless = "#result == null")
  public QuoteOfTheDay save(final QuoteOfTheDayRequest request) throws QotdNotFoundException {
    final var savedQotd = dsl.insertInto(QOTD)
      .set(QOTD.QUOTE_ID, request.quoteId())
      .set(QOTD.CREATION_DATE, request.creationDate())
      .returning()
      .fetchOneInto(QuotesOfTheDayRecord.class);
    if (savedQotd == null) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return QOTD_MAPPER.toQuoteOfTheDay(savedQotd);
  }

  /**
   * Finds a Quote of the Day by date in the cache and returns the QOTD object.
   *
   * @param date The date for which to find the Quote of the Day.
   * @return The QuoteOfTheDay object for the given date.
   * @throws QotdNotFoundException If the Quote of the Day for the given date is not found.
   */
  @Cacheable(value = "qotd", key = "#date.atStartOfDay()", unless = "#result == null")
  public QuoteOfTheDay findByDate(final LocalDate date) throws QotdNotFoundException {
    final var qotd = dsl.selectFrom(QOTD)
      .where(QOTD.CREATION_DATE.between(date.atStartOfDay(), date.atTime(23, 59, 59)))
      .fetchOneInto(QuotesOfTheDayRecord.class);
    if (qotd == null) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return QOTD_MAPPER.toQuoteOfTheDay(qotd);
  }
}
