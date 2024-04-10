package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.Quotes;
import de.zedalite.quotes.data.jooq.tables.QuotesOfTheDay;
import de.zedalite.quotes.data.jooq.tables.records.QuotesOfTheDayRecord;
import de.zedalite.quotes.data.mapper.QuoteOfTheDayMapper;
import de.zedalite.quotes.data.model.Quote;
import de.zedalite.quotes.data.model.QuoteOfTheDay;
import de.zedalite.quotes.data.model.QuoteOfTheDayRequest;
import de.zedalite.quotes.exceptions.QotdNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class GroupQuoteOfTheDayRepository {

  private static final QuoteOfTheDayMapper QOTD_MAPPER = QuoteOfTheDayMapper.INSTANCE;

  private static final Quotes QUOTES = Quotes.QUOTES.as("quotes");

  private static final QuotesOfTheDay QOTD = QuotesOfTheDay.QUOTES_OF_THE_DAY.as("quotes_of_the_day");

  private static final String QOTD_NOT_FOUND = "QuoteOfTheDay not found";

  private final DSLContext dsl;

  public GroupQuoteOfTheDayRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  public QuoteOfTheDay save(final Integer id, final QuoteOfTheDayRequest request) {
    final var savedQotd = dsl.insertInto(QOTD)
      .set(QOTD.GROUP_ID, id)
      .set(QOTD.QUOTE_ID, request.quoteId())
      .set(QOTD.CREATION_DATE, request.creationDate())
      .returning()
      .fetchOptionalInto(QuotesOfTheDayRecord.class);
    if (savedQotd.isEmpty()) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return QOTD_MAPPER.mapToQuoteOfTheDay(savedQotd.get());
  }

  @Cacheable(value = "qotd", key = "{#id,#date}", unless = "#result == null")
  public Quote findByDate(final Integer id, final LocalDate date) throws QotdNotFoundException {
    final var qotd = dsl.select(QUOTES)
      .from(QOTD.join(QUOTES).on(QOTD.QUOTE_ID.eq(QUOTES.ID)))
      .where(QOTD.GROUP_ID.eq(id))
      .and(QOTD.CREATION_DATE.eq(date))
      .fetchOptionalInto(Quote.class);
    if (qotd.isEmpty()) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return qotd.get();
  }
}
