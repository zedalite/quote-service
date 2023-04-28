package de.zedproject.quotesapi.data.repository;

import de.zedproject.jooq.quote.tables.Quotes;
import de.zedproject.jooq.quote.tables.records.QuotesRecord;
import de.zedproject.quotesapi.data.model.QuoteRequest;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuoteRepository {
  private static final Quotes QUOTES = Quotes.QUOTES.as("Quotes");
  private static final Logger LOG = LoggerFactory.getLogger(QuoteRepository.class);
  private final DSLContext dsl;

  public QuoteRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  public QuotesRecord save(final QuoteRequest quote) {
    final var savedQuote = dsl.insertInto(QUOTES)
            .set(QUOTES.AUTHOR, quote.author())
            .set(QUOTES.DATETIME, quote.datetime())
            .set(QUOTES.TEXT, quote.text())
            .set(QUOTES.SUBTEXT, quote.subtext())
            .returning()
            .fetchOneInto(QuotesRecord.class);
    LOG.debug("Quote saved, quote={}", savedQuote);
    return savedQuote;
  }

  public List<QuotesRecord> findAll() {
    final var quotes = dsl.selectFrom(QUOTES)
            .fetchInto(QuotesRecord.class);
    LOG.debug("Quotes found, count={}", quotes.size());
    return quotes;
  }

  public List<QuotesRecord> findAllByIds(final List<Integer> ids) {
    final var quotes = dsl.selectFrom(QUOTES)
            .where(QUOTES.ID.in(ids))
            .fetchInto(QuotesRecord.class);
    LOG.debug("Quotes found, count={}", quotes.size());
    return quotes;
  }

  public List<Integer> findAllIds() {
    final var quotesIds = dsl.select(QUOTES.ID)
            .from(QUOTES)
            .fetchInto(Integer.class);
    LOG.debug("QuoteIds found, count={}", quotesIds.size());
    return quotesIds;
  }

  public QuotesRecord findById(final Integer id) {
    final var quote = dsl.selectFrom(QUOTES)
            .where(QUOTES.ID.eq(id))
            .fetchOneInto(QuotesRecord.class);
    LOG.debug("Quote found, quote={}", quote);
    return quote;
  }

  public QuotesRecord update(final Integer id, final QuoteRequest quote) {
    dsl.update(QUOTES)
            .set(QUOTES.AUTHOR, quote.author())
            .set(QUOTES.DATETIME, quote.datetime())
            .set(QUOTES.TEXT, quote.text())
            .set(QUOTES.SUBTEXT, quote.subtext())
            .where(QUOTES.ID.eq(id))
            .execute();
    final var updatedQuote = findById(id);
    LOG.debug("Quote updated, quote={}", updatedQuote);
    return updatedQuote;
  }

  public QuotesRecord delete(final Integer id) {
    final var deletedQuote = findById(id);
    dsl.deleteFrom(QUOTES)
            .where(QUOTES.ID.eq(id))
            .returning()
            .fetchOneInto(QuotesRecord.class);
    LOG.debug("Quote deleted, quote={}", deletedQuote);
    return deletedQuote;
  }

  public Integer count() {
    final Integer count = dsl.fetchCount(QUOTES);
    LOG.debug("Quotes available, count={}", count);
    return count;
  }
}
