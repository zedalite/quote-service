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

@Repository
public class QuoteOfTheDayRepository {

  public static final QuotesOfTheDay QOTD = QuotesOfTheDay.QUOTES_OF_THE_DAY.as("QuotesOfTheDay");

  public static final QuoteOfTheDayMapper QOTD_MAPPER = QuoteOfTheDayMapper.INSTANCE;
  public static final String QOTD_NOT_FOUND = "QuoteOfTheDay not found";

  private final DSLContext dsl;

  public QuoteOfTheDayRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  @CachePut(value = "qotd", key = "#request.datetime().toLocalDate().atStartOfDay()", unless = "#result == null")
  public QuoteOfTheDay save(final QuoteOfTheDayRequest request) throws QotdNotFoundException {
    final var savedQotd = dsl.insertInto(QOTD)
      .set(QOTD.QUOTE_ID, request.quoteId())
      .set(QOTD.DATETIME, request.datetime())
      .returning()
      .fetchOneInto(QuotesOfTheDayRecord.class);
    if (savedQotd == null) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return QOTD_MAPPER.toQuoteOfTheDay(savedQotd);
  }

  @Cacheable(value = "qotd", key = "#date.atStartOfDay()", unless = "#result == null")
  public QuoteOfTheDay findByDate(final LocalDate date) throws QotdNotFoundException {
    final var qotd = dsl.selectFrom(QOTD)
      .where(QOTD.DATETIME.between(date.atStartOfDay(), date.atTime(23, 59, 59)))
      .fetchOneInto(QuotesOfTheDayRecord.class);
    if (qotd == null) throw new QotdNotFoundException(QOTD_NOT_FOUND);
    return QOTD_MAPPER.toQuoteOfTheDay(qotd);
  }
}
